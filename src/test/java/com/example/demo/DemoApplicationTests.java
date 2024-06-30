package com.example.demo;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class DemoApplicationTests {
    private RestHighLevelClient client;

    @BeforeEach
    void init() {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.223.133:9200")
        ));
    }

    @Test
    void test() {
        System.out.println(client);
    }

    //索引的增删改查
    //不同依赖版本的api不一样
    @Test
    void createIndex() throws IOException {
        String idxName="items";
        CreateIndexRequest request=new CreateIndexRequest(idxName);
        request.source(MAPPING_TEMPLATE, XContentType.JSON);//指定了映射、也可不指定
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    //修改索引实际上是增加映射
    @Test
    void updateIndex() throws IOException {
        String idxName="items";
        PutMappingRequest request=new PutMappingRequest(idxName);
        request.source(TO_ADD_TEMPLATE, XContentType.JSON);
        client.indices().putMapping(request, RequestOptions.DEFAULT);
    }

    @Test
    void selectAllIndex() throws IOException {
//        GetIndicesRequest request = new GetIndicesRequest("_all");
//        GetIndicesResponse response = client.indices().get(request, RequestOptions.DEFAULT);
//
//        String[] indices = response.getIndices();
//        Arrays.sort(indices);
//        for (String index : indices) {
//            System.out.println(index);
//        }
    }

    @Test
    void selectIndex() throws IOException {
        GetIndexRequest request=new GetIndexRequest("items");
        System.out.println(client.indices().get(request, RequestOptions.DEFAULT));
    }
    @Test
    void deleteIndex() throws IOException {
        String idxName="items";
        DeleteIndexRequest request=new DeleteIndexRequest(idxName);
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    @Test
    void isExistIndex() throws IOException {
        GetIndexRequest request=new GetIndexRequest("items");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }



    @AfterEach
    void close() throws Exception {
        client.close();
    }

    static final String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      },\n" +
            "      \"price\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"stock\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"image\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"category\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"brand\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"sold\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    static final String TO_ADD_TEMPLATE = "{\n" +
            "  \"properties\": {\n" +
            "    \"commentCount\": {\n" +
            "      \"type\": \"integer\"\n" +
            "    },\n" +
            "    \"isAD\": {\n" +
            "      \"type\": \"boolean\"\n" +
            "    },\n" +
            "    \"updateTime\": {\n" +
            "      \"type\": \"date\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

}
