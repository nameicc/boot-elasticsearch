package com.tingyu.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tingyu.model.Person;
import com.tingyu.util.ESConnParam;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 基础增删改查
 **/
@Component
public class Test1 {

    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final String INDEX = "person";

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

    public Person insertPerson(Person person) {
        person.setPersonId(UUID.randomUUID().toString());
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("personId", person.getPersonId());
        dataMap.put("name", person.getName());
        dataMap.put("number", person.getNumber());
        IndexRequest indexRequest = new IndexRequest(INDEX).id(person.getPersonId()).source(dataMap);
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return person;
    }

    public Person getPersonById(String id) {
        GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
        GetResponse getResponse = null;
        try {
            getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResponse == null ? null : objectMapper.convertValue(getResponse.getSourceAsMap(), Person.class);
    }

    public Person updatePerson(Person person) {
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, person.getPersonId()).fetchSource(true);
        UpdateResponse updateResponse = null;
        try {
            String personJson = objectMapper.writeValueAsString(person);
            updateRequest.doc(personJson, XContentType.JSON);
            updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateResponse == null ? null : objectMapper.convertValue(updateResponse.getGetResult().sourceAsMap(), Person.class);
    }

    public void deletePerson(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
        try {
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {

        }
    }

}
