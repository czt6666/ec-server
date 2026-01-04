package com.bistu.ecadmin.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 养老服务主体信息实体类
 */
@Data
@ApiModel("养老服务主体信息")
public class Station implements Serializable {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty(value = "主体名称（如XX养老服务中心）", required = true)
    private String name;

    @ApiModelProperty(value = "注册地址", required = true)
    private String registeredAddress;

    @ApiModelProperty(value = "经营地址", required = true)
    private String businessAddress;

    @ApiModelProperty("注册地址纬度")
    private BigDecimal registeredLatitude;

    @ApiModelProperty("注册地址经度")
    private BigDecimal registeredLongitude;

    @ApiModelProperty("经营地址纬度")
    private BigDecimal businessLatitude;

    @ApiModelProperty("经营地址经度")
    private BigDecimal businessLongitude;

    @ApiModelProperty(value = "统一社会信用代码", required = true)
    private String unifiedSocialCreditCode;

    @ApiModelProperty(value = "法定代表人", required = true)
    private String legalRepresentative;

    @ApiModelProperty("注册资本（万元）")
    private BigDecimal registeredCapital;

    @ApiModelProperty("成立日期")
    private LocalDate establishmentDate;

    @ApiModelProperty("营业期限（如：长期/2020-01-01至2050-01-01）")
    private String businessTerm;

    @ApiModelProperty(value = "官方联系电话", required = true)
    private String officialPhone;

    @ApiModelProperty(value = "紧急联系人", required = true)
    private String emergencyContact;

    @ApiModelProperty(value = "紧急联系电话", required = true)
    private String emergencyPhone;

    @ApiModelProperty("官方邮箱")
    private String officialEmail;

    @ApiModelProperty("主体类型ID（关联主体类型表，暂时可空）")
    private Long subjectTypeId;

    @ApiModelProperty("服务模式（多选，逗号分隔，如：机构住养,日间照料,上门服务,综合型）")
    private String serviceMode;

    @ApiModelProperty("养老机构设立许可证编号")
    private String elderlyLicenseNo;

    @ApiModelProperty("医疗机构执业许可证编号（如有）")
    private String medicalLicenseNo;

    @ApiModelProperty("食品经营许可证编号")
    private String foodLicenseNo;

    @ApiModelProperty("消防验收合格证明编号")
    private String fireAcceptanceNo;

    @ApiModelProperty(value = "营业状态（1：营业中；2：暂停营业；3：已注销）", example = "1")
    private Integer businessStatus;

    @ApiModelProperty("总床数")
    private Integer totalBeds;

    @ApiModelProperty("房型配置（多选，逗号分隔，如：单人,多人）")
    private String roomConfig;

    @ApiModelProperty("护理等级（多选，逗号分隔，如：自理,半自理,非自理）")
    private String careLevel;

    @ApiModelProperty("价格区间（如：2000-5000元/月）")
    private String priceRange;

    @ApiModelProperty("机构简介")
    private String introduction;

    @ApiModelProperty("环境照片（多个URL用逗号分隔）")
    private String environmentPhotos;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
    @ApiModelProperty("收藏数量")
    private Long collectNumber;
    
    @ApiModelProperty("当前用户是否已收藏：0-未收藏，1-已收藏")
    private Integer isCollect;
}

