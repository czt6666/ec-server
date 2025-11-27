package com.bistu.ecadmin.service;

import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.pojo.Result;

/**
 * 菜品Service接口
 */
public interface DishService {
    
    /**
     * 新增菜品
     * @param params 请求参数
     * @return Result对象
     */
    Result<?> addDish(JSONObject params);
    
    /**
     * 分页查询菜品列表
     * @param params 查询参数
     * @return Result对象
     */
    Result<?> listDishes(JSONObject params);
    
    /**
     * 获取菜品详情
     * @param params 查询参数
     * @return Result对象
     */
    Result<?> getDishDetail(JSONObject params);
    
    /**
     * 更新菜品
     * @param params 请求参数
     * @return Result对象
     */
    Result<?> updateDish(JSONObject params);
    
    /**
     * 删除菜品
     * @param params 请求参数
     * @return Result对象
     */
    Result<?> deleteDish(JSONObject params);
}