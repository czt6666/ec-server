package com.bistu.ecadmin.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("用户收藏")
public class UserCollect implements Serializable {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @ApiModelProperty(value = "收藏对象类型：plan/activity", required = true)
    private String targetType;

    @ApiModelProperty(value = "收藏对象ID", required = true)
    private String targetId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}

