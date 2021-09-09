package com.scistor.estools.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scistor.estools.data.RandomDate;
import com.scistor.estools.data.RandomIp;
import com.scistor.estools.data.RandomPhoneNumber;
import com.scistor.estools.data.RandomText;
import com.scistor.estools.entity.IndexEntity;
import com.scistor.estools.service.EsService;
import com.scistor.estools.util.HttpClient;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpOptions;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

/**
 * @author zhuling
 * @Description
 * @create 2021-09-06 10:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    @Value("${es.host}")
    public String host;

    @Value("${es.port}")
    public int port;

    @Value("${es.scheme}")
    public String scheme;

    public static RestHighLevelClient client = null;

    private void init() {
        try {
            createClient("127.0.0.1",9200);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public synchronized String createClient(String host,int port) {
        if (client == null) {
            HttpHost httpHost = new HttpHost(host, port, "http");

            client = new RestHighLevelClient(RestClient.builder(httpHost)
                    .setRequestConfigCallback(builder -> {
                        //连接超时2分钟
                        builder.setConnectTimeout(2 * 60 * 1000);
                        //socket长连接时间30分钟（默认30秒）
                        builder.setSocketTimeout(30 * 60 * 1000);
                        // 连接请求超时30分钟
                        builder.setConnectionRequestTimeout(30 * 60 * 1000);
                        return builder;
                    }));

        }
        return "连接"+host+":"+port+"节点成功";
    }


    @org.junit.Test
    public void getIndexs() throws IOException {
        // 客户端连接
        init();
        // 获取所有index列表：http://192.168.3.187:9200/_cat/indices?format=json
        String url = "http://127.0.0.1:9200/_cat/indices?format=json";
        String s = HttpClient.doGet(url);
//        JSONArray indexs = (JSONArray) JSONObject.toJSON(s);
        JSONArray indexs = (JSONArray) JSONObject.parse(s);
        List<IndexEntity> list = new ArrayList<>();
        indexs.forEach(index->{
            JSONObject jsonObject = (JSONObject) index;
            IndexEntity indexEntity = new IndexEntity();
            indexEntity.setHealth(jsonObject.getString("health"));
            indexEntity.setStatus(jsonObject.getString("status"));
            indexEntity.setIndex(jsonObject.getString("index"));
            indexEntity.setUuid(jsonObject.getString("uuid"));
            indexEntity.setPri(jsonObject.getString("pri"));
            indexEntity.setRep(jsonObject.getString("rep"));
            indexEntity.setDocsCount(jsonObject.getString("docs.count"));
            indexEntity.setDocsDeleted(jsonObject.getString("docs.deleted"));
            indexEntity.setStoreSize(jsonObject.getString("store.size"));
            indexEntity.setPriStoreSize(jsonObject.getString("pri.store.size"));
            list.add(indexEntity);
        });

        list.forEach(x->{
            System.out.println(x.toString());
        });
    }

    @org.junit.Test
    public void testGetMapping() throws IOException {
        init();
        String url = "http://127.0.0.1:9200/irs_card";
        String s = HttpClient.doGet(url);
        JSONObject result = (JSONObject) JSONObject.parse(s);
        JSONObject irs_message = result.getJSONObject("irs_card");
        JSONObject mapping = result.getJSONObject("irs_card").getJSONObject("mappings").getJSONObject("properties");
        System.out.println(JSONObject.toJSONString(mapping));
//        return mapping;
    }

    @org.junit.Test
    public void testAddrecord() throws IOException {
        String json = "{\n" +
                "    \"destIp\":\"192.167.3.197,47.168.108.97\",\n" +
                "    \"callerNumber\": \"phone_0\",\n" +
                "    \"destCity\": \"中国,英国,美国,日本,俄罗斯,法国\",\n" +
                "    \"filePath\": \"/opt/calculator,/usr/local/message\",\n" +
                "    \"oriCity\": \"中国,英国,美国,日本,俄罗斯,法国\",\n" +
                "    \"messageType\": \"邮件,短信,语音,名片\",\n" +
                "    \"eventTime\": \"2020-01-01,2021-12-31\",\n" +
                "    \"card\": \"zhangsan,lisi,wangwu\"\n" +
                "}";
        JSONObject data = (JSONObject) JSONObject.parse(json);
        init();
        String url = "http://127.0.0.1:9200/irs_card";
        String s = HttpClient.doGet(url);
        JSONObject result = (JSONObject) JSONObject.parse(s);

        JSONObject mapping = result.getJSONObject("irs_card").getJSONObject("mappings").getJSONObject("properties");
        Iterator iter = mapping.entrySet().iterator();
        JSONObject record = new JSONObject();
        String key = "";
        String value = "";
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            key = entry.getKey().toString();
            value = JSONObject.parseObject(entry.getValue().toString()).getString("type");
            if ("long".equals(value)) {
                record.put(key,new Random().nextLong());
            } else if ("date".equals(value)) {
                String[] string = data.getString(key).split(",");
                record.put(key, RandomDate.randomDate(string[0],string[1]));
            } else if ("integer".equals(value)) {
                record.put(key, new Random().nextInt());
            } else if ("text".equals(value) || "keyword".equals(value)) {
                String inputValue = data.getString(key);
                if (inputValue.startsWith("phone")){
                    String[] split = inputValue.split("_");
                    record.put(key, RandomPhoneNumber.createMobile(Integer.valueOf(split[1])));
                } else if (inputValue.startsWith("ip")){
                    String[] string = inputValue.split("_");
                    record.put(key, RandomIp.getRandomIp());
                } else if (inputValue.startsWith("email")){
                    String[] string = inputValue.split("_");
                    record.put(key, RandomText.getRandomText(string));
                } else {
                    String[] string = inputValue.split(",");
                    record.put(key, RandomText.getRandomText(string));
                }
            }
        }
        System.out.println(JSONObject.toJSONString(record));
    }
}
