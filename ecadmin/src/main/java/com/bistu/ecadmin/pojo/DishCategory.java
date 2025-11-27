package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 菜品分类实体类
 */
@Data
public class DishCategory {
    /**
     * 主键ID（分类唯一标识）
     */
    private Long id;

    /**
     * 关联门店ID
     */
    private Long restaurantId;

    /**
     * 菜品分类名称
     */
    private String categoryName;

    /**
     * 排序号
     */
    private Integer sortNum;

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