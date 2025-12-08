package com.bistu.ecadmin.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 旅游公司主体信息
 */
@Data
@ApiModel("旅游公司")
public class TourCompany implements Serializable {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty(value = "公司名称", required = true)
    private String name;

    @ApiModelProperty("公司简称")
    private String shortName;

    @ApiModelProperty(value = "注册地址", required = true)
    private String registeredAddress;

    @ApiModelProperty("注册地址纬度")
    private BigDecimal registeredLatitude;

    @ApiModelProperty("注册地址经度")
    private BigDecimal registeredLongitude;

    @ApiModelProperty("经营地址")
    private String businessAddress;

    @ApiModelProperty("经营地址纬度")
    private BigDecimal businessLatitude;

    @ApiModelProperty("经营地址经度")
    private BigDecimal businessLongitude;

    @ApiModelProperty("经营范围")
    private String businessScope;

    @ApiModelProperty(value = "营业状态：1营业中 2暂停 3已注销", example = "1")
    private Integer businessStatus;

    @ApiModelProperty("统一社会信用代码")
    private String unifiedSocialCreditCode;

    @ApiModelProperty("法定代表人")
    private String legalRepresentative;

    @ApiModelProperty("注册资本（万元）")
    private BigDecimal registeredCapital;

    @ApiModelProperty("成立日期")
    private LocalDate establishmentDate;

    @ApiModelProperty("营业期限")
    private String businessTerm;

    @ApiModelProperty("官方联系电话")
    private String officialPhone;

    @ApiModelProperty("紧急联系人")
    private String emergencyContact;

    @ApiModelProperty("紧急联系电话")
    private String emergencyPhone;

    @ApiModelProperty("官方邮箱")
    private String officialEmail;

    @ApiModelProperty("简介/特色亮点")
    private String intro;

    @ApiModelProperty("Logo 图 URL")
    private String logoUrl;

    @ApiModelProperty("官方网站")
    private String website;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("逻辑状态：1正常 0删除")
    private Integer status;
}

