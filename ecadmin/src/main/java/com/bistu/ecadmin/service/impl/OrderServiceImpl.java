package com.bistu.ecadmin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.dao.OrderDao;
import com.bistu.ecadmin.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    @Transactional
    public Long createOrder(JSONObject params) throws Exception {
        // 生成订单号（时间戳+随机数）
        String orderNo = "ORD" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
        
        // 获取菜品ID列表
        JSONArray dishIds = params.getJSONArray("dishIds");
        if (dishIds == null || dishIds.size() == 0) {
            throw new Exception("请选择菜品");
        }
        
        // 获取菜品数量列表（如果没有提供，默认每个菜品数量为1）
        JSONArray quantities = params.getJSONArray("quantities");
        
        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<JSONObject> orderItems = new ArrayList<>();
        
        for (int i = 0; i < dishIds.size(); i++) {
            Long dishId = dishIds.getLong(i);
            // 查询菜品信息
            JSONObject dish = orderDao.getDishById(dishId);
            if (dish == null) {
                throw new Exception("菜品不存在: " + dishId);
            }
            
            // 获取菜品数量
            int quantity = 1;
            if (quantities != null && i < quantities.size()) {
                quantity = quantities.getInteger(i);
            }
            
            // 计算小计金额
            BigDecimal dishPrice = dish.getBigDecimal("dishPrice");
            BigDecimal subtotalAmount = dishPrice.multiply(new BigDecimal(quantity));
            
            // 累加总金额
            totalAmount = totalAmount.add(subtotalAmount);
            
            // 构建订单项
            JSONObject orderItem = new JSONObject();
            orderItem.put("dishId", dishId);
            orderItem.put("dishName", dish.getString("dishName"));
            orderItem.put("dishPrice", dishPrice);
            orderItem.put("quantity", quantity);
            orderItem.put("subtotalAmount", subtotalAmount);
            
            orderItems.add(orderItem);
        }

        // 准备订单主表数据
        JSONObject orderInfo = new JSONObject();
        orderInfo.put("orderNo", orderNo);
        orderInfo.put("userId", params.getLong("userId"));
        orderInfo.put("restaurantId", params.getLong("restaurantId"));
        orderInfo.put("orderStatus", 1); // 默认待处理状态
        orderInfo.put("totalAmount", totalAmount);
        orderInfo.put("remark", params.getString("remark"));

        // 插入订单主表
        orderDao.addOrder(orderInfo);
        Long orderId = orderInfo.getLong("id");
        if (orderId == null) {
            throw new Exception("订单创建失败，无法获取订单ID");
        }
        
        // 插入订单项
        for (JSONObject item : orderItems) {
            JSONObject orderItem = new JSONObject();
            orderItem.put("orderId", orderId);
            orderItem.put("dishId", item.getLong("dishId"));
            orderItem.put("dishName", item.getString("dishName"));
            orderItem.put("dishPrice", item.getBigDecimal("dishPrice"));
            orderItem.put("quantity", item.getInteger("quantity"));
            orderItem.put("subtotalAmount", item.getBigDecimal("subtotalAmount"));
            orderDao.addOrderItem(orderItem);
        }

        return orderId;
    }

    @Override
    public JSONObject getOrderList(JSONObject params) throws Exception {
        // 获取当前用户ID
        Long currentUserId = params.getLong("currentUserId");
        if (currentUserId == null) {
            throw new Exception("用户ID不能为空");
        }
        
        // 检查用户是否为管理员
        boolean isAdmin = isAdminUser(currentUserId);
        
        // 如果不是管理员，需要获取用户对应的餐厅ID
        if (!isAdmin) {
            Long restaurantId = getRestaurantIdByUserId(currentUserId);
            if (restaurantId != null) {
                params.put("restaurantId", restaurantId);
                System.out.println("非管理员用户，只显示餐厅ID为 " + restaurantId + " 的订单");
            } else {
                System.out.println("非管理员用户，但未找到对应的餐厅，将不显示任何订单");
                // 如果未找到对应的餐厅，设置一个不存在的餐厅ID，确保不返回任何订单
                params.put("restaurantId", -1L);
            }
        } else {
            System.out.println("管理员用户，可以查看所有订单");
        }
        
        // 计算分页参数
        Integer pageNum = params.getInteger("pageNum");
        Integer pageRow = params.getInteger("pageRow");
        Integer offset = (pageNum - 1) * pageRow;
        params.put("offset", offset);
        params.put("limit", pageRow);

        // 查询订单列表
        List<JSONObject> orderList = orderDao.getOrderList(params);
        Integer totalCount = orderDao.getOrderCount(params);

        System.out.println("查询到 " + orderList.size() + " 个订单");

        JSONObject result = new JSONObject();
        result.put("records", orderList);
        result.put("total", totalCount);
        result.put("pageNum", pageNum);
        result.put("pageRow", pageRow);

        return result;
    }
    
    /**
     * 检查用户是否为管理员
     */
    private boolean isAdminUser(Long userId) {
        // 项目中使用硬编码的管理员用户ID
        return userId != null && userId == 10011;
    }
    
    /**
     * 根据用户ID获取餐厅ID
     */
    @Override
    public Long getRestaurantIdByUserId(Long userId) throws Exception {
        // 查询用户对应的餐厅ID
        return orderDao.getRestaurantIdByUserId(userId);
    }

    @Override
    public JSONObject getOrderDetail(Long orderId) throws Exception {
        JSONObject orderDetail = orderDao.getOrderDetail(orderId);
        if (orderDetail == null) {
            throw new Exception("订单不存在");
        }
        
        // 查询订单详情项
        List<JSONObject> orderItems = orderDao.getOrderItemsByOrderId(orderId);
        orderDetail.put("orderItems", orderItems);
        
        return orderDetail;
    }

    @Override
    public void updateOrderStatus(Long orderId, Integer orderStatus) throws Exception {
        JSONObject params = new JSONObject();
        params.put("orderId", orderId);
        params.put("orderStatus", orderStatus);
        params.put("updateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        orderDao.updateOrderStatus(params);
    }

    @Override
    public JSONObject getOrderStatistics(JSONObject params) throws Exception {
        // 获取统计信息
        List<JSONObject> statisticsList = orderDao.getOrderStatistics(params);
        
        // 计算汇总数据
        JSONObject summary = new JSONObject();
        int totalOrderCount = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (JSONObject stat : statisticsList) {
            totalOrderCount += stat.getIntValue("orderCount");
            totalAmount = totalAmount.add(stat.getBigDecimal("totalAmount"));
        }
        
        summary.put("statisticsList", statisticsList);
        summary.put("totalOrderCount", totalOrderCount);
        summary.put("totalAmount", totalAmount);
        
        return summary;
    }

    @Override
    public void updateOrderStatistics(String statDate) throws Exception {
        // 从订单表统计指定日期的数据并更新统计表
        JSONObject params = new JSONObject();
        params.put("statDate", statDate);
        
        // 先删除当天的统计数据
        orderDao.deleteOrderStatisticsByDate(statDate);
        
        // 插入新的统计数据
        orderDao.insertOrderStatisticsFromOrders(params);
    }
}