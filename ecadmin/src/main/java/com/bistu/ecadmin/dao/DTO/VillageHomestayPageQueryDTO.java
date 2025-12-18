package com.bistu.ecadmin.dao.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

@Data
@ApiModel("民宿分页查询DTO")
public class VillageHomestayPageQueryDTO implements Serializable {
    
    @ApiModelProperty("页码")
    private Integer page;
    
    @ApiModelProperty("每页大小")
    private Integer pageSize;
    
    @ApiModelProperty("所属乡村ID")
    private Integer villageId;
    
    @ApiModelProperty("民宿名称（模糊查询）")
    private String homestayName;
    
    @ApiModelProperty("地址（模糊查询）")
    private String address;
    
    @ApiModelProperty("营业状态：1-营业 2-暂停营业 3-已下架")
    private Integer status;
    
    @ApiModelProperty("星级/等级：0-未评 1-5星")
    private Integer starLevel;
    
    @ApiModelProperty("负责人姓名（模糊查询）")
    private String contactName;
    
    @ApiModelProperty("关键词搜索（支持民宿名称、地址、负责人姓名）")
    private String keyword;
    
    @ApiModelProperty("当前用户ID（用于判断是否收藏）")
    private Long userId;
}