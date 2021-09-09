package com.scistor.estools.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scistor.estools.entity.DataConfig;
import com.scistor.estools.entity.IndexEntity;
import com.scistor.estools.mapper.DataConfigMapper;
import com.scistor.estools.util.AssertUtil;
import com.scistor.estools.util.FileUtil;
import com.scistor.estools.util.HttpClient;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author zhuling
 * @Description
 * @create 2021-09-03 15:43
 */
@Component
public class EsService {

    private static final Logger logger = LoggerFactory.getLogger(EsService.class);

    public static RestHighLevelClient client = null;

    private static String host;

    private static int port;

    @Autowired
    private DataConfigMapper dataConfigMapper;

    /**
     * 创建连接客户端 RestHighLevelClient client = new RestHighLevelClient(
     * RestClient.builder( new HttpHost("localhost", 9200, "http"), new
     * HttpHost("localhost", 9201, "http")));
     */
    public synchronized String createClient(String host, int port) {
        this.host = host;
        this.port = port;
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
        return "连接节点"+host+":"+port+"成功！";
    }

    /**
     * 获取当前节点所有的索引列表信息
     * @return
     */
    public List<IndexEntity> getIndexs() {
        List<IndexEntity> result = new ArrayList<>();
        try {
            String url = "http://"+host+":"+port+"/_cat/indices?format=json";
            String s = HttpClient.doGet(url);
            JSONArray indexs = (JSONArray) JSONObject.parse(s);

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
                result.add(indexEntity);
            });
        } catch (Exception e) {
            logger.error("",e);
        }
        return result;
    }

    /**
     * 获取制定索引的mapping信息
     * @param index
     * @return
     */
    public JSONObject getIndexTextMapping(String index) {
        List<String> result = new ArrayList<>();
        JSONObject parse = new JSONObject();
        try {
            String url = "http://"+host+":"+port+"/"+index;
            String s = HttpClient.doGet(url);
            parse = (JSONObject) JSONObject.parse(s);
            JSONObject mapping = parse.getJSONObject(index).getJSONObject("mappings").getJSONObject("properties");
            Iterator iter = mapping.entrySet().iterator();
            String key = "";
            String type = "";
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                key = entry.getKey().toString();
                type = entry.getValue().toString();
                result.add(key);
            }
        } catch (Exception e) {
            logger.error("",e);
        }
        return parse;
    }

    /**
     * 获取制定索引的mapping信息
     * @param index
     * @return
     */
    public List<DataConfig> getIndexSubMapping(String index) {
        List<DataConfig> result = new ArrayList<>();
        JSONObject parse = new JSONObject();
        try {
            // 先查询数据库是否存在配置
            List<DataConfig> dataConfigs = dataConfigMapper.selectList(new QueryWrapper<DataConfig>().eq("index_name", index));
            if (dataConfigs.size() > 0)
                return dataConfigs;
            String url = "http://"+host+":"+port+"/"+index;
            String s = HttpClient.doGet(url);
            parse = (JSONObject) JSONObject.parse(s);
            JSONObject mapping = parse.getJSONObject(index).getJSONObject("mappings").getJSONObject("properties");
            Iterator iter = mapping.entrySet().iterator();
            String key = "";
            String type = "";
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                key = entry.getKey().toString();
                JSONObject o = (JSONObject) JSONObject.parse(entry.getValue().toString());
                type = o.getString("type");
                DataConfig dataConfig = new DataConfig();
                dataConfig.setIndexName(index);
                dataConfig.setField(key);
                dataConfig.setType(type);
                result.add(dataConfig);
            }
        } catch (Exception e) {
            logger.error("",e);
        }
        return result;
    }

    /**
     * 新增索引
     * @param indexName
     * @param file
     * @return
     */
    public Boolean addIndex(String indexName, MultipartFile file) {
        Boolean result = false;
        try {
            if (client == null) {
                throw new Exception("请先连接es节点！");
            }
            // 判断文件是否是json文件
            if (file.getOriginalFilename().endsWith("json")) {
                // 读取文件创建索引
                String mapping = FileUtil.readFile(file);
                logger.info("{}的mapping映射文件内容：{}",indexName,mapping);
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                createIndexRequest.source(mapping, XContentType.JSON);
                createIndex(createIndexRequest);
                logger.info("{}索引创建完成",indexName);
                result = true;
            }
        } catch (Exception e){
            logger.error("",e.getMessage());
        }
        return result;
    }

    /**
     *
     * @param index
     * @return
     */
    public boolean addrecord(String index, String record) {
        try {
            if (client == null) {
                throw new Exception("请先连接es节点！");
            }
            IndexRequest request = new IndexRequest(index);
            request.source(record, XContentType.JSON);
//            request.type("_doc");
            client.index(request, RequestOptions.DEFAULT);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    // 创建索引
    public void createIndex(CreateIndexRequest createIndexRequest) throws Exception {
        AssertUtil.notNullObject(createIndexRequest);
        String index = createIndexRequest.index();
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        if (indexExist(getIndexRequest)) {
            throw new Exception("索引已存在");
        }
        client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    // 判断索引是否存在
    public boolean indexExist(GetIndexRequest getIndexRequest) {
        AssertUtil.notNullObject(getIndexRequest);
        boolean exists = false;
        try {
            if (client == null) {
                throw new Exception("请先连接es节点！");
            }
            exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            return exists;
        } catch (Exception e) {
            logger.error("", e);
        }
        return exists;
    }

    public boolean deleteIndex(String indexName) {
        AssertUtil.notNullObject(indexName);
        try {
            if (client == null) {
                throw new Exception("请先连接es节点！");
            }
            client.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
        } catch (Exception e) {
            logger.error("", e);
        }
        return true;
    }

}
