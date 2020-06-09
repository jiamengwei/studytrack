package com.elastic.controller;

import com.elastic.entity.Project;
import com.elastic.entity.Prompt;
import com.elastic.mapper.ProjectMapper;
import com.elastic.repository.ProjectRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
public class ProjectController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ProjectRepository projectRepository;


    @SneakyThrows
    @RequestMapping("prompt")
    public Prompt prompt(String q) {
        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                new HttpHost("192.168.131.139", 9200, "http")));

        List<Project> projects = new ArrayList<>();

        SearchRequest searchRequest = new SearchRequest("project");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders
            .matchQuery("projectName", q)
            .fuzziness(Fuzziness.AUTO);

        String[] includeFields = new String[]{"projectName", "address"};
        String[] excludeFields = new String[]{};
        searchSourceBuilder.fetchSource(includeFields, excludeFields);


        searchSourceBuilder.query(matchQueryBuilder);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field projectNameHighlight = new HighlightBuilder.Field("projectName");
        highlightBuilder.preTags("<em style='color:red;'>").postTags("</em>");
        projectNameHighlight.highlighterType("plain");
        highlightBuilder.field(projectNameHighlight);

        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits responseHits = searchResponse.getHits();
        log.info("totalHits:{}", responseHits.getTotalHits());
        SearchHit[] hits = responseHits.getHits();

        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            log.info("project : {}", hit.getSourceAsString());
            Project project = objectMapper.readValue(json, Project.class);
            projects.add(project);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("projectName");
            if (highlightField != null) {
                log.info("highlight field name : {}", highlightField.getName());
                Text[] fieldFragments = highlightField.getFragments();
                for (Text text : fieldFragments) {
                    project.setProjectName(text.toString());
                    log.info("highlight text : {}", text.string());
                }
            }
        }

        Prompt prompt = new Prompt(projects);
        return prompt;
    }

    @SneakyThrows
    @GetMapping("test")
    public long promptTest() {
        CountRequest countRequest = new CountRequest("project");
        return restHighLevelClient.count(countRequest, RequestOptions.DEFAULT).getCount();
    }


    @SneakyThrows
    @GetMapping("test2")
    public List<Project> promptTest2(String q) {
        SearchRequest searchRequest = new SearchRequest("project");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("projectName", q);
        searchSourceBuilder.query(matchQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        return null;
    }

    @PostMapping("save")
    public Project save() {
        Project project = new Project().setStartTime(LocalDateTime.now());
        Project save = projectRepository.save(project);
        return save;
    }

    @PostMapping("transferToEs")
    public List<Project> transferToEs() {
        //查询所有Project数据转储到ES中
        List<Project> projects = projectMapper.selectList(null);
        Iterable<Project> result = projectRepository.saveAll(projects);
        return projects;
    }

    /**
     * 多字段模糊高亮查询
     *
     * @param q
     * @return
     */
    @GetMapping("multiMatchQuery")
    public Prompt multiMatchQuery(String q) {
        //构造一个bool查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //查询address、projectName字段
        boolQuery.must(QueryBuilders.multiMatchQuery(q, "address", "projectName", "projectName.pinyin").fuzziness(Fuzziness.AUTO));
        //构造查询条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(boolQuery)
            //设置查询字段过滤
            .withSourceFilter(new SourceFilter() {
                //包含字段
                @Override
                public String[] getIncludes() {
                    return new String[]{"projectName", "address", "startTime"};
                }

                //排除字段
                @Override
                public String[] getExcludes() {
                    return new String[]{""};
                }
            })
            //查询的索引
            .withIndices("project")
            //类型
            .withTypes("project")
            //设置高亮字段
            .withHighlightFields(
                new HighlightBuilder.Field("address").preTags("<span style='color:red'>").postTags("</span>"),
                new HighlightBuilder.Field("projectName").preTags("<span style='color:red'>").postTags("</span>"))
            .build();
        //获取搜索结果
        SearchResponse query = elasticsearchOperations.query(searchQuery, searchResponse -> searchResponse);

        SearchHit[] hits = query.getHits().getHits();
        //获取高亮信息填充到返回结果对应的字段中
        List<Project> collect =
            Stream.of(hits)
                .map(h -> {
                    Project project = new Project();
                    try {
                        project = objectMapper.readValue(h.getSourceAsString(), Project.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    Map<String, HighlightField> highlightFields = h.getHighlightFields();
                    if (highlightFields.get("projectName") != null) {
                        project.setProjectName(highlightFields.get("projectName").getFragments()[0].toString());
                    }
                    if (highlightFields.get("address") != null) {
                        project.setAddress(highlightFields.get("address").getFragments()[0].toString());
                    }
                    return project;
                }).collect(Collectors.toList());
        Prompt prompt = new Prompt();
        prompt.setItems(collect);
        return prompt;
    }
}
