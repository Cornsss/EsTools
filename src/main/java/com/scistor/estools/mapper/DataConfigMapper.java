package com.scistor.estools.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scistor.estools.entity.DataConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DataConfigMapper extends BaseMapper<DataConfig> {
    List<String> queryDataConfigGroup();
}
