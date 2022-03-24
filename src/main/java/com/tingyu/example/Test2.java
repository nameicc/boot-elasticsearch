package com.tingyu.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tingyu.util.ESConnParam;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 简单聚合
 **/
@Component
public class Test2 {

    private RestHighLevelClient restHighLevelClient;

    private final String INDEX = "training_course";

    private final String TYPE = "_doc";

    @PostConstruct
    public void makeConnection() {
        if (restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(ESConnParam.HOST, ESConnParam.PORT_ONE, ESConnParam.SCHEME), new HttpHost(ESConnParam.HOST, ESConnParam.PORT_TWO, ESConnParam.SCHEME)));
        }
    }

    public void closeConnection() {
        try {
            restHighLevelClient.close();
            restHighLevelClient = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void basicAggs() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchSourceBuilder.aggregation(AggregationBuilders.sum("sum").field("fees"));
        searchSourceBuilder.aggregation(AggregationBuilders.avg("avg").field("fees"));
        searchSourceBuilder.aggregation(AggregationBuilders.min("min").field("fees"));
        searchSourceBuilder.aggregation(AggregationBuilders.max("max").field("fees"));
        searchSourceBuilder.aggregation(AggregationBuilders.cardinality("cardinality").field("fees"));
        searchSourceBuilder.aggregation(AggregationBuilders.count("count").field("fees"));

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHitArr = searchResponse.getHits().getHits();
                for (SearchHit searchHit : searchHitArr) {
                    Map<String, Object> map = searchHit.getSourceAsMap();
                    System.out.println("Index data: " + Arrays.toString(map.entrySet().toArray()));
                }
            }

            Sum sum = searchResponse.getAggregations().get("sum");
            double sumValue = sum.getValue();
            System.out.println("Aggs sum: " + sumValue);

            Avg avg = searchResponse.getAggregations().get("avg");
            double avgValue = avg.getValue();
            System.out.println("Aggs avg: " + avgValue);

            Min min = searchResponse.getAggregations().get("min");
            double minValue = min.getValue();
            System.out.println("Aggs min: " + minValue);

            Max max = searchResponse.getAggregations().get("max");
            double maxValue = max.getValue();
            System.out.println("Aggs max: " + maxValue);

            Cardinality cardinality = searchResponse.getAggregations().get("cardinality");
            double cardinalityValue = cardinality.getValue();
            System.out.println("Aggs cardinality: " + cardinalityValue);

            ValueCount count = searchResponse.getAggregations().get("count");
            double countValue = count.getValue();
            System.out.println("Aggs count: " + countValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void basicBucket() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchSourceBuilder.aggregation(AggregationBuilders.terms("distinct_instructor").field("instructor.keyword"));
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHitArr = searchResponse.getHits().getHits();
                for (SearchHit searchHit : searchHitArr) {
                    Map<String, Object> map = searchHit.getSourceAsMap();
                    System.out.println("Index data: " + Arrays.toString(map.entrySet().toArray()));
                }
            }

            List<String> list = new ArrayList<String>();
            Terms terms = searchResponse.getAggregations().get("distinct_instructor");
            for (Terms.Bucket bucket: terms.getBuckets()) {
                list.add(bucket.getKeyAsString() + ": " + bucket.getDocCount());
            }
            System.out.println("Distinct list values: " + list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
