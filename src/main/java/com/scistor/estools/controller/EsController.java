package com.scistor.estools.controller;

import com.alibaba.fastjson.JSONObject;
import com.scistor.estools.base.ViewResponse;
import com.scistor.estools.entity.IndexEntity;
import com.scistor.estools.service.EsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhuling
 * @Description
 * @create 2021-09-03 16:01
 */
@RestController
@RequestMapping("/es")
@Api(tags = "Es接口")
@CrossOrigin(originPatterns = "*",allowedHeaders = "*",methods = {},allowCredentials = "true")
public class EsController {

    @Autowired
    private EsService esService;

    @PostMapping("/connectEs/{host}/{port}")
    @ApiOperation("es连接")
    public ViewResponse connectEs(@PathVariable String host, @PathVariable int port){
        try {
            return ViewResponse.success(esService.createClient(host, port));
        }  catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }

    @GetMapping("/getIndex")
    @ApiOperation("获取所有索引列表")
    public ViewResponse getIndexs(){
        try {
            return ViewResponse.success(esService.getIndexs());
        }  catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }

    @PostMapping("/createIndex/{indexName}")
    @ApiOperation("创建索引")
    public ViewResponse addIndex(MultipartFile file,@PathVariable String indexName){
        try {
            return ViewResponse.success(esService.addIndex(indexName,file));
        }  catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }

    @PostMapping("/deleteIndex/{indexName}")
    @ApiOperation("删除索引")
    public ViewResponse deleteIndex(@PathVariable String indexName){
        try {
            return ViewResponse.success(esService.deleteIndex(indexName));
        }  catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }

    @GetMapping("/getIndexTextMapping/{index}")
    @ApiOperation("查看指定索引mapping文本信息")
    public ViewResponse getIndexTextMapping(@PathVariable String index){
        try {
            return ViewResponse.success(esService.getIndexTextMapping(index));
        }  catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }

    @GetMapping("/getIndexSubMapping/{index}")
    @ApiOperation("查看指定索引mapping数据项信息")
    public ViewResponse getIndexSubMapping(@PathVariable String index){
        try {
            return ViewResponse.success(esService.getIndexSubMapping(index));
        }  catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }

    @PostMapping("/addrecord/{index}")
    @ApiOperation("新增索引数据")
    public ViewResponse addrecord(@PathVariable String index, @RequestParam String jsonObject){
        try {
            return ViewResponse.success(esService.addrecord(index,jsonObject));
        }  catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }
}
