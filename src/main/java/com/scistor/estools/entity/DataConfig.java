package com.scistor.estools.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author zhuling
 * @Description
 * @create 2021-09-08 15:49
 */
@Data
public class DataConfig {

    private String field;

    private String type;

    private String config;

    private String indexName;

}
