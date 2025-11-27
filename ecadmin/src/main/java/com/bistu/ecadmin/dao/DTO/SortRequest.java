package com.bistu.ecadmin.dao.DTO;

import lombok.Data;

/**
 * 排序请求DTO类
 */
@Data
public class SortRequest {
    /**
     * 菜品分类ID
     */
    private Long id;
    
    /**
     * 排序号
     */
    private Integer sortNum;
}