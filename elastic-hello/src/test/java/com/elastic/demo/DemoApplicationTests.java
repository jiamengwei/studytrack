package com.elastic.demo;

import com.elastic.entity.User;
import com.elastic.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Import(value = {com.elastic.config.RestClientConfig.class, com.elastic.config.ObjectMapperConfig.class})
@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Test
    void basicTest() {
        User userA = new User(1, "张三", "北京市丰台区五里店", LocalDateTime.of(1990, 10, 22, 12, 30, 21));

        //添加到es
        User save = userRepository.save(userA);

        //根据id查询
        Optional<User> userById = userRepository.findById(1);
        userById.orElseThrow(() -> new IllegalStateException("添加失败"));

        //根据用户名查询
        User userByUsername = userRepository.findByUsername("张三");
        Assert.assertNotNull(userByUsername);

        //根据id删除
        userRepository.deleteById(1);
        boolean exists = userRepository.existsById(1);
        Assert.assertFalse(exists);
    }

    /**
     * 中文分词高亮模糊查询
     */
    @Test
    void searchTest() {
        User userA = new User(1, "张三", "北京市丰台区五里店", LocalDateTime.of(1990, 10, 22, 12, 30, 21));
        userRepository.save(userA);

        String q = "北京丰台";
        //构造一个bool查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //查询address、projectName字段
        boolQuery.must(QueryBuilders.multiMatchQuery(q, "username", "address").fuzziness(Fuzziness.AUTO));
        //构造查询条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(boolQuery)
            //设置查询字段过滤
            .withSourceFilter(new SourceFilter() {
                //包含字段
                @Override
                public String[] getIncludes() {
                    return new String[]{"username", "address", "birthday"};
                }

                //排除字段
                @Override
                public String[] getExcludes() {
                    return new String[]{""};
                }
            })
            //查询的索引
            .withIndices("user")
            //类型
            .withTypes("user")
            //设置高亮字段
            .withHighlightFields(
                new HighlightBuilder.Field("address").preTags("<span style='color:red'>").postTags("</span>"),
                new HighlightBuilder.Field("username").preTags("<span style='color:red'>").postTags("</span>"))
            .build();
        //获取搜索结果
        SearchResponse query = elasticsearchOperations.query(searchQuery, searchResponse -> searchResponse);
        SearchHit[] hits = query.getHits().getHits();
        //获取高亮信息填充到返回结果对应的字段中
        List<User> userList =
            Stream.of(hits)
                .map(hit -> {
                    User user = new User();
                    try {
                        user = objectMapper.readValue(hit.getSourceAsString(), User.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    if (highlightFields.get("username") != null) {
                        user.setUsername(highlightFields.get("username").getFragments()[0].toString());
                    }
                    if (highlightFields.get("address") != null) {
                        user.setAddress(highlightFields.get("address").getFragments()[0].toString());
                    }
                    return user;
                }).collect(Collectors.toList());

        Assert.assertNotNull(userList);
        userList.stream().forEach(System.out::println);
    }

    /**
     * 拼音中文混合分词高亮模糊查询
     */
    @Test
    void searchPinYinTest() {
        User userA = new User(1, "张三", "北京市丰台区五里店", LocalDateTime.of(1990, 10, 22, 12, 30, 21));
        userRepository.save(userA);

        String q = "zs";
        //构造一个bool查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //查询address、projectName字段
        boolQuery.must(QueryBuilders.multiMatchQuery(q, "username", "username.pinyin", "address").fuzziness(Fuzziness.AUTO));
        //构造查询条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(boolQuery)
            //设置查询字段过滤
            .withSourceFilter(new SourceFilter() {
                //包含字段
                @Override
                public String[] getIncludes() {
                    return new String[]{"username", "address", "birthday"};
                }

                //排除字段
                @Override
                public String[] getExcludes() {
                    return new String[]{""};
                }
            })
            //查询的索引
            .withIndices("user")
            //类型
            .withTypes("user")
            //设置高亮字段
            .withHighlightFields(
                new HighlightBuilder.Field("address").preTags("<span style='color:red'>").postTags("</span>"),
                new HighlightBuilder.Field("username").preTags("<span style='color:red'>").postTags("</span>"))
            .build();
        //获取搜索结果
        SearchResponse query = elasticsearchOperations.query(searchQuery, searchResponse -> searchResponse);
        SearchHit[] hits = query.getHits().getHits();
        //获取高亮信息填充到返回结果对应的字段中
        List<User> userList =
            Stream.of(hits)
                .map(hit -> {
                    User user = new User();
                    try {
                        user = objectMapper.readValue(hit.getSourceAsString(), User.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    if (highlightFields.get("username") != null) {
                        user.setUsername(highlightFields.get("username").getFragments()[0].toString());
                    }
                    if (highlightFields.get("address") != null) {
                        user.setAddress(highlightFields.get("address").getFragments()[0].toString());
                    }
                    return user;
                }).collect(Collectors.toList());

        Assert.assertNotNull(userList);
        userList.stream().forEach(System.out::println);
    }
}
