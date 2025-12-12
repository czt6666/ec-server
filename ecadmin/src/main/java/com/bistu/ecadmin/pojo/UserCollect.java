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

    @ApiModelProperty("收藏对象名称（扩展字段，查询列表时返回）")
    private String targetName;

    @ApiModelProperty("该对象的收藏总数（扩展字段，查询列表时返回）")
    private Long collectCount;

    @ApiModelProperty("当前用户是否已收藏（扩展字段，查询列表时返回，列表中的记录都是true）")
    private Boolean isCollected;
}

