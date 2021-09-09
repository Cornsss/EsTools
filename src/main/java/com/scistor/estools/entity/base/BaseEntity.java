/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.scistor.estools.entity.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 通用字段， is_del 根据需求自行添加
 * @author Zheng Jie
 * @Date 2019年10月24日20:46:32
 */
@Getter
@Setter
public class BaseEntity implements Serializable {

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateBy;

    @ApiModelProperty(value = "创建时间", hidden = true)
    private Timestamp createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    private Timestamp updateTime;

    /* 分组校验 */
    public @interface Create {}

    /* 分组校验 */
    public @interface Update {}

}
