package com.scistor.estools.controller;

import com.scistor.estools.base.ViewResponse;
import com.scistor.estools.entity.DataConfig;
import com.scistor.estools.service.DataConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhuling
 * @Description
 * @create 2021-09-08 15:46
 */
@RestController
@RequestMapping("/config")
@CrossOrigin(originPatterns = "*",allowedHeaders = "*",methods = {},allowCredentials = "true")
public class ConfigController {

    @Autowired
    private DataConfigService dataConfigService;

    @PostMapping("/addDataConfig")
    @ApiOperation("添加数据项配置")
    public ViewResponse addDataConfig(@RequestBody List<DataConfig> dataConfig){
        try {
            return ViewResponse.success(dataConfigService.addDataConfig(dataConfig));
        } catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }

    @GetMapping("/queryDataConfig")
    @ApiOperation("查询数据项配置列表")
    public ViewResponse queryDataConfig(){
        try {
            return ViewResponse.success(dataConfigService.queryDataConfig());
        } catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }

    @GetMapping("/queryDataConfigGroup")
    @ApiOperation("查询数据项配置列表")
    public ViewResponse queryDataConfigGroup(){
        try {
            return ViewResponse.success(dataConfigService.queryDataConfigGroup());
        } catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }
}
