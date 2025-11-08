package com.bistu.ecadmin.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Cart {
    @ApiModelProperty(value = "主键ID")
    private Integer id;
    @ApiModelProperty(value = "前台用户ID")
    private Integer userId;
    @ApiModelProperty(value = "商品规格ID")
    private Integer skuId;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
