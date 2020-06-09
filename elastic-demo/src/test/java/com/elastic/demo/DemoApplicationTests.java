package com.elastic.demo;

import com.elastic.entity.Project;
import com.elastic.mapper.ProjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
@MapperScan("com.elastic.mapper")
class DemoApplicationTests {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProjectMapper projectMapper;

    @Test
    void contextLoads() {
    }


    @SneakyThrows
    @Test
    void initIndex(){
        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                new HttpHost("192.168.131.139", 9200, "http")));

        List<Project> projects = projectMapper.selectList(null);

        BulkRequest bulkRequest = new BulkRequest();

        projects.stream().forEach(project -> {
            IndexRequest request = new IndexRequest("project");  //posts : index名称
            request.id(project.getTid());
            String jsonString = null;
            try {
                jsonString = objectMapper.writeValueAsString(project);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            request.source(jsonString, XContentType.JSON);
            bulkRequest.add(request);
        });

        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        log.info("hasFailures : {}", bulkResponse.hasFailures());
    }


    @SneakyThrows
    @Test
    void search(){
        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                new HttpHost("192.168.131.139", 9200, "http")));

        List<Project> projects = projectMapper.selectList(null);

        SearchRequest searchRequest = new SearchRequest("project");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MatchQueryBuilder matchQueryBuilder = QueryBuilders
            .matchQuery("projectName", "测试");

        searchSourceBuilder.query(matchQueryBuilder);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field projectNameHighlight = new HighlightBuilder.Field("projectName");
        highlightBuilder.preTags("<em>").postTags("</em>");
        projectNameHighlight.highlighterType("plain");
        highlightBuilder.field(projectNameHighlight);

        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits responseHits = searchResponse.getHits();
        log.info("totalHits:{}", responseHits.getTotalHits());
        SearchHit[] hits = responseHits.getHits();
        for (SearchHit hit : hits){
            log.info("project : {}", hit.getSourceAsString());
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("projectName");
            if (highlightField != null){
                log.info("highlight field name : {}", highlightField.getName());
                Text[] fieldFragments = highlightField.getFragments();
                for (Text text : fieldFragments){
                    log.info("highlight text : {}", text.string());
                }
            }
        }
    }
}
