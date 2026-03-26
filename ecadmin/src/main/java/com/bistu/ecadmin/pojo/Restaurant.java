package com.bistu.ecadmin.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 餐饮商铺实体类
 */
@Data
public class Restaurant implements Serializable {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty(value = "关联用户ID", required = true)
    private Long userId;

    @ApiModelProperty(value = "所属村ID", required = true)
    private Integer villageId;

    @ApiModelProperty(value = "门店名称（≤100字符）", required = true)
    private String name;

    @ApiModelProperty(value = "开始营业时间（HH:mm）", required = true)
    private String businessStartTime;

    @ApiModelProperty(value = "结束营业时间（HH:mm）", required = true)
    private String businessEndTime;

    @ApiModelProperty("门店Logo（单张图片URL）")
    private String logoUrl;

    @ApiModelProperty(value = "门店地址（≤200字符）", required = true)
    private String address;

    @ApiModelProperty("纬度")
    private BigDecimal coordinateLat;

    @ApiModelProperty("经度")
    private BigDecimal coordinateLng;

    @ApiModelProperty("联系电话（≤20字符）")
    private String phone;

    @ApiModelProperty("门店公告（≤500字符）")
    private String notice;

    @ApiModelProperty("证照图片JSON，含营业执照/食品许可证（单类≤15张）")
    private String licenseUrls;

    @ApiModelProperty(value = "状态：1-正常 0-停用", example = "1")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("排序字段")
    private Integer sortOrder;

    private String villageName;
    private String userName;
    @ApiModelProperty("收藏数量")
    private Long collectNumber;
    
    @ApiModelProperty("当前用户是否已收藏：0-未收藏，1-已收藏")
    private Integer isCollect;
}
