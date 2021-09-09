package com.scistor.estools.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scistor.estools.data.RandomDate;
import com.scistor.estools.data.RandomIp;
import com.scistor.estools.data.RandomPhoneNumber;
import com.scistor.estools.data.RandomText;
import com.scistor.estools.entity.DataConfig;
import com.scistor.estools.mapper.DataConfigMapper;
import com.scistor.estools.service.EsService;
import com.scistor.estools.util.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * @author zhuling
 * @Description
 * @create 2021-09-08 15:58
 */
@Slf4j
@Component
public class InsertRecordTask {

    @Autowired
    private DataConfigMapper dataConfigMapper;

    @Autowired
    private EsService esService;

    public void run(String index){
        try {
            log.info("run 执行成功,参数：{}",index);
            List<DataConfig> dataConfigs = dataConfigMapper.selectList(new QueryWrapper<DataConfig>().eq("index_name", index));
            if (dataConfigs.size() > 0) {
                JSONObject record = new JSONObject();
                for (DataConfig dataConfig : dataConfigs) {
                    String type = dataConfig.getType();
                    String config = dataConfig.getConfig();
                    String field = dataConfig.getField();
                    if ("long".equals(type)) { // 生成输入数字范围内的long
                        long start = Long.valueOf(config);
                        record.put(field,new Random().nextLong()+start);
                    } else if ("date".equals(type)) { // 生成输入日期范围内的date
                        String[] string = config.split(",");
                        record.put(field, RandomDate.randomDate(string[0],string[1]));
                    } else if ("integer".equals(type)) { // 生成输入数字范围内的integer
                        String[] s1 = config.split(",");
                        int start = Integer.valueOf(s1[0]);
                        int end = Integer.valueOf(s1[1]);
                        record.put(field, new Random().nextInt(end)+start);
                    } else if ("float".equals(type)) { // 生成随机浮点数
                        record.put(field, "0.0");
                    } else if ("text".equals(type)) { // 生成输入文本数组范围内的随机文本
                        record.put(field, RandomText.getRandomText(config.split(",")));
                    } else if ("phone".equals(type)) { // 生成移动、联通、电信的随机号码
                        record.put(field, RandomPhoneNumber.createMobile(Integer.valueOf(config)));
                    } else if ("ip".equals(type)) { // 生成随机ip
                        record.put(field, RandomIp.getRandomIp());
                    } else if ("email".equals(type)) { // 生成随机邮箱号
                        record.put(field, config);
                    } else {
                        record.put(field, config);
                    }
                }

                // TODO: es插入记录
                boolean addrecord = esService.addrecord(index, JSONObject.toJSONString(record));
                if (!addrecord) {
                    log.error("添加记录失败：{}",JSONObject.toJSONString(record));
                } else {
                    log.info("添加记录成功：{}",JSONObject.toJSONString(record));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
