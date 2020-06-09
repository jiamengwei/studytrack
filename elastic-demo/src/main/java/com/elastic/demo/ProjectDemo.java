package com.elastic.demo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.util.Map;

@Slf4j
public class ProjectDemo {
    @SneakyThrows
    public static void main(String[] args) {
        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                new HttpHost("192.168.131.139", 9200, "http")));

        SearchRequest searchRequest = new SearchRequest("project");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("project_name", "测试");
//            .fuzziness(Fuzziness.AUTO)
//            .prefixLength(3)
//            .maxExpansions(10);

//        searchSourceBuilder.query(matchQueryBuilder);
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();

        for (SearchHit hit : searchHits) {
            String sourceStr = hit.getSourceAsString();
            log.info(sourceStr);
        }

        for (SearchHit hit : searchHits){
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField field = highlightFields.get("project_name");
            if (field == null){
                continue;
            }
            //获取一个或多个包含突出显示字段内容的片段
            for (Text text : field.fragments()){
                String highlightStr = text.toString();
                log.info("highlight: {}",highlightStr);
            }

        }
    }
}
