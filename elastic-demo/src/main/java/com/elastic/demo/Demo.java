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
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Demo {
    @SneakyThrows
    public static void main(String[] args) {
        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                new HttpHost("192.168.131.139", 9200, "http")));
        SearchRequest searchRequest = new SearchRequest();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //模糊查询
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("message", "out");
//            .analyzer("ik_max_word")
//            .fuzziness(Fuzziness.AUTO)
//            .prefixLength(3)    //设置忽略的前缀长度为3
//            .maxExpansions(10);  //限制将产生的模糊选项的总数量

        searchSourceBuilder.query(matchQueryBuilder);
        //设置查询时间
        searchSourceBuilder.timeout(new TimeValue(5, TimeUnit.SECONDS));

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //为name字段设置高亮
        HighlightBuilder.Field highlightName =
            new HighlightBuilder.Field("user");
        highlightBuilder.preTags("<em>").postTags("</em>");
        //设置字段高亮类型
        highlightName.highlighterType("plain");
        highlightBuilder.field(highlightName);

        HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("email");
        HighlightBuilder.Field highlightMessage = new HighlightBuilder.Field("message");
        highlightMessage.highlighterType("plain");
        highlightBuilder.field(highlightUser);
        highlightBuilder.field(highlightMessage);

        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits searchHits = searchResponse.getHits();
        long totalHits = searchHits.getTotalHits();
        log.info("totalHits:{}", totalHits);

        for (SearchHit hit : searchHits) {
            String sourceStr = hit.getSourceAsString();
            System.out.println(sourceStr);
        }

        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits.getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            //获取字段title的高亮信息
            HighlightField highlight = highlightFields.get("message");
            if (highlight == null){
                continue;
            }
            //获取一个或多个包含突出显示字段内容的片段
            for (Text text : highlight.fragments()){
                String highlightStr = text.toString();
                log.info("highlight: {}",highlightStr);
            }
        }
    }
}
