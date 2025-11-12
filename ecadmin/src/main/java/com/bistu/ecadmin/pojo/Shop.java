package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 店铺实体类
 */
@Data
public class Shop {
    /**
     * 店铺ID
     */
    private Long id;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺缩写（用于生成默认用户名）
     */
    private String shopAbbreviation;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 经营状态：0-停业，1-营业
     */
    private Integer businessStatus;

    /**
     * 所属村
     */
    private String village;

    /**
     * 店铺简介
     */
    private String shopIntro;

    /**
     * 店铺头像
     */
    private String shopAvatar;

    /**
     * 店铺地址
     */
    private String shopAddress;

    /**
     * 资质凭证（JSON格式存储多个文件路径）
     */
    private String qualificationFiles;

    /**
     * 关联的用户ID（商家账号）
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    private int displayNo;
}
