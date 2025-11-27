package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品实体类
 */
@Data
public class Dish {
    /**
     * 主键ID（菜品唯一标识）
     */
    private Long id;

    /**
     * 关联门店ID（关联restaurant表的id）
     */
    private Long restaurantId;

    /**
     * 关联菜品分类ID（关联dish_category表的id）
     */
    private Long categoryId;

    /**
     * 菜品名称（最多100字符）
     */
    private String dishName;

    /**
     * 菜品封面图（仅1张，存储图片地址）
     */
    private String coverImgUrl;

    /**
     * 菜品状态：1=上架，0=下架
     */
    private Integer dishStatus;

    /**
     * 菜品售价（0-999.99，保留2位小数）
     */
    private BigDecimal price;

    /**
     * 单位（最多5字符，如"份""个"）
     */
    private String unit;

    /**
     * 菜品概要（最多30字符）
     */
    private String summary;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0=未删除，1=已删除
     */
    private Integer isDeleted;
}