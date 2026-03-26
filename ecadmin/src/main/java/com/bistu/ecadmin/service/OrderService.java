package com.bistu.ecadmin.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
     * 获取订单列表
     */
    JSONObject getOrderList(JSONObject params) throws Exception;
    
    /**
     * 获取订单详情
     */
    JSONObject getOrderDetail(Long orderId) throws Exception;
    
    /**
     * 更新订单状态
     */
    void updateOrderStatus(Long orderId, Integer orderStatus) throws Exception;
    
    /**
     * 获取订单统计信息
     */
    JSONObject getOrderStatistics(JSONObject params) throws Exception;
    
    /**
     * 更新订单统计表
     */
    void updateOrderStatistics(String statDate) throws Exception;
    
    /**
     * 创建订单
     */
    Long createOrder(JSONObject params) throws Exception;
    
    /**
     * 根据用户ID获取餐厅ID
     */
    Long getRestaurantIdByUserId(Long userId) throws Exception;
    
    /**
     * 根据用户ID获取订单列表
     */
    JSONObject getOrderListByUserId(Long userId) throws Exception;
}