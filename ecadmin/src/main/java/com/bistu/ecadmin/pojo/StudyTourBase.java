package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 研学基地实体类
 */
@Data
public class StudyTourBase {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 研学基地名称
     */
    private String baseName;

    /**
     * 运行单位
     */
    private String operationUnit;

    /**
     * 基地详细地址
     */
    private String address;

    /**
     * 地址纬度
     */
    private BigDecimal latitude;

    /**
     * 地址经度
     */
    private BigDecimal longitude;

    /**
     * 法定代表人
     */
    private String legalRepresentative;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 资质证明（多个URL用逗号分隔）
     */
    private String qualificationCert;

    /**
     * 基地特色说明
     */
    private String featureDesc;

    /**
     * 营业状态（1：营业中；2：暂停营业；3：已注销）
     */
    private Integer businessStatus;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 归属商户（后台用户）ID
     */
    private Long userId;

    /**
     * 归属商户用户名
     */
    private String userName;





    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}