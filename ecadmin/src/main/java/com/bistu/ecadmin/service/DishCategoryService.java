package com.bistu.ecadmin.service;

import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;

/**
 * 菜品分类Service接口
 */
public interface DishCategoryService {
    
    /**
     * 新增菜品分类
     * @param params 请求参数
     * @return Result对象
     */
    Result<?> addDishCategory(JSONObject params);
    
    /**
     * 查询菜品分类列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param categoryName 菜品分类名称
     * @param restaurantName 餐厅名称
     * @param status 状态
     * @param userId 用户ID
     * @return 分页结果
     */
    Result<PageResult> listDishCategories(
            Integer page,
            Integer pageSize,
            String categoryName,
            String restaurantName,
            Integer status,
            Long userId
    );
    
    /**
     * 更新菜品分类
     * @param params 请求参数
     * @return Result对象
     */
    Result<?> updateDishCategory(JSONObject params);
    
    /**
     * 删除菜品分类
     * @param params 请求参数
     * @return Result对象
     */
    Result<?> deleteDishCategory(JSONObject params);
    
    /**
     * 更新菜品分类排序号
     * @param id 菜品分类ID
     * @param sortNum 排序号
     * @return 影响行数
     */
    int updateSortNum(Long id, Integer sortNum);
}