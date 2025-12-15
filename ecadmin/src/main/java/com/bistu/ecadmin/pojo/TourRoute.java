package com.bistu.ecadmin.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 旅游路线信息
 */
@Data
@ApiModel("旅游路线")
public class TourRoute implements Serializable {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty(value = "路线名称", required = true)
    private String name;

    @ApiModelProperty("类型名称，如红色旅游/绿色旅游/组合")
    private String routeType;

    @ApiModelProperty("主题标签，逗号分隔")
    private String themeTags;

    @ApiModelProperty("起点地址")
    private String originAddress;

    @ApiModelProperty("起点纬度")
    private BigDecimal originLat;

    @ApiModelProperty("起点经度")
    private BigDecimal originLng;

    @ApiModelProperty("终点地址")
    private String destAddress;

    @ApiModelProperty("终点纬度")
    private BigDecimal destLat;

    @ApiModelProperty("终点经度")
    private BigDecimal destLng;

    @ApiModelProperty("行程天数")
    private Integer days;

    @ApiModelProperty("行程难度：轻松/适中/挑战等")
    private String difficulty;

    @ApiModelProperty("行程安排（文字描述）")
    private String itinerary;

    @ApiModelProperty(value = "经营状态：1发布 2进行中 3暂停", example = "1")
    private Integer bizStatus;

    @ApiModelProperty("目标人群")
    private String targetCrowd;

    @ApiModelProperty("价格区间，如：2000-5000元/人")
    private String priceRange;

    @ApiModelProperty("安全措施")
    private String safetyMeasures;

    @ApiModelProperty("服务保障")
    private String serviceGuarantee;

    @ApiModelProperty("费用包含")
    private String costIncluded;

    @ApiModelProperty("费用不包含")
    private String costExcluded;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("逻辑状态：1正常 0删除")
    private Integer status;
    
    @ApiModelProperty("收藏数量")
    private Long collectNumber;
}

