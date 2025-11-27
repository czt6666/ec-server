package com.bistu.ecadmin.dao.DTO;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 菜品分类DTO类
 */
@Data
public class DishCategoryDTO {
    /**
     * 主键ID（分类唯一标识）
     */
    private Long id;

    /**
     * 菜品分类名称
     */
    private String categoryName;

    /**
     * 餐厅名称
     */
    private String restaurantName;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 菜品创建时间
     */
    private LocalDateTime dishCreateTime;
}