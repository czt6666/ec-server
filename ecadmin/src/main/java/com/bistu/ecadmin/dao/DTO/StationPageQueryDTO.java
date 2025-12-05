package com.bistu.ecadmin.dao.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("驿站分页查询DTO")
public class StationPageQueryDTO implements Serializable {

    @ApiModelProperty("页码")
    private Integer page;

    @ApiModelProperty("每页大小")
    private Integer pageSize;

    @ApiModelProperty("驿站名称（模糊查询）")
    private String name;

    @ApiModelProperty("营业状态（1：营业中；2：暂停营业；3：已注销）")
    private Integer status;

    @ApiModelProperty("关键词搜索（支持驿站名称、注册地址、经营地址）")
    private String keyword;
}

