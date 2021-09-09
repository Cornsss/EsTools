package com.scistor.estools.controller;

import com.scistor.estools.base.ViewResponse;
import com.scistor.estools.service.QuartzJobService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhuling
 * @Description
 * @create 2021-09-08 10:35
 */
@RestController
@Api(tags = "定时任务controller")
@RequestMapping("/quartz")
@CrossOrigin(originPatterns = "*",allowedHeaders = "*",methods = {},allowCredentials = "true")
public class QuartzJobController {


    @Autowired
    private QuartzJobService quartzJobService;

    @GetMapping("/execution/{id}")
    public ViewResponse execution(@PathVariable Long id){
        try {
            return ViewResponse.success(quartzJobService.execution(id));
        } catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }

    @GetMapping("/querySchedule")
    public ViewResponse querySchedule(){
        try {
            return ViewResponse.success(quartzJobService.querySchedule());
        } catch (Exception e) {
            return ViewResponse.failed(e.getMessage());
        }
    }

}
