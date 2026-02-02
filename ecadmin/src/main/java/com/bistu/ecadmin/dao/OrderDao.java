package com.bistu.ecadmin.dao;

import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 * 订单数据访问接口
 */
public interface OrderDao {
    
    /**
     * 添加订单
     */
    void addOrder(JSONObject params);
    
    /**
     * 添加订单项
     */
    void addOrderItem(JSONObject params);
    
    /**
     * 获取订单列表
     */
    List<JSONObject> getOrderList(JSONObject params);
    
    /**
     * 获取订单总数
     */
    Integer getOrderCount(JSONObject params);
    
    /**
     * 获取订单详情
     */
    JSONObject getOrderDetail(Long orderId);
    
    /**
     * 根据订单ID获取订单项
     */
    List<JSONObject> getOrderItemsByOrderId(Long orderId);
    
    /**
     * 更新订单状态
     */
    void updateOrderStatus(JSONObject params);
    
    /**
     * 获取订单统计信息
     */
    List<JSONObject> getOrderStatistics(JSONObject params);
    
    /**
     * 根据日期删除统计信息
     */
    void deleteOrderStatisticsByDate(String statDate);
    
    /**
     * 从订单表插入统计数据
     */
    void insertOrderStatisticsFromOrders(JSONObject params);
    
    /**
     * 根据ID查询菜品信息
     */
    JSONObject getDishById(Long dishId);
    
    /**
     * 根据用户ID查询餐厅ID
     */
    Long getRestaurantIdByUserId(Long userId);
}