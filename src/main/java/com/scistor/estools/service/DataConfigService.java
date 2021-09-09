package com.scistor.estools.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scistor.estools.base.ViewResponse;
import com.scistor.estools.entity.DataConfig;
import com.scistor.estools.mapper.DataConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuling
 * @Description
 * @create 2021-09-08 15:52
 */
@Service
@Slf4j
public class DataConfigService {

    @Autowired
    private DataConfigMapper dataConfigMapper;

    public Boolean addDataConfig(List<DataConfig> dataConfig) {
        Boolean result = false;
        try {
            // 先删除当前索引下的配置
            dataConfigMapper.delete(new QueryWrapper<DataConfig>().eq("index_name",dataConfig.get(0).getIndexName()));
            // 再插入当前所有配置信息
            dataConfig.forEach(x->{
                dataConfigMapper.insert(x);
            });
            result = true;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return result;
    }

    public List<DataConfig> queryDataConfig() {
        try {
            return dataConfigMapper.selectList(null);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    public List<String> queryDataConfigGroup() {
        try {
            return dataConfigMapper.queryDataConfigGroup();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }
}
