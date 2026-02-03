package com.bistu.ecadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 餐厅订单管理控制器
 */
@RestController
@RequestMapping("/restaurant/orders")
public class RestaurantOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 获取餐厅订单列表
     */
    @RequiresPermissions("dishOrder:list")
    @GetMapping("/list")
    public Result getRestaurantOrderList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageRow,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(required = false) Integer orderStatus,
            @RequestHeader("x-user-id") Long currentUserId) {
        
        JSONObject params = new JSONObject();
        params.put("pageNum", pageNum);
        params.put("pageRow", pageRow);
        params.put("currentUserId", currentUserId);
        if (orderNo != null) params.put("orderNo", orderNo);
        if (userId != null) params.put("userId", userId);
        if (restaurantId != null) params.put("restaurantId", restaurantId);
        if (orderStatus != null) params.put("orderStatus", orderStatus);

        try {
            JSONObject result = orderService.getOrderList(params);
            return Result.success(result, "获取餐厅订单列表成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取餐厅订单详情
     */
    @GetMapping("/detail/{orderId}")
    public Result getRestaurantOrderDetail(
            @PathVariable Long orderId,
            @RequestHeader("x-user-id") Long currentUserId) {
        try {
            // 检查用户权限
            boolean isAdmin = isAdminUser(currentUserId);
            if (!isAdmin) {
                // 非管理员用户只能查看自己餐厅的订单
                Long restaurantId = getRestaurantIdByUserId(currentUserId);
                if (restaurantId != null) {
                    // 检查订单是否属于该餐厅
                    JSONObject orderDetail = orderService.getOrderDetail(orderId);
                    if (orderDetail != null) {
                        Long orderRestaurantId = orderDetail.getLong("restaurantId");
                        if (!restaurantId.equals(orderRestaurantId)) {
                            return Result.error("无权限查看该订单");
                        }
                    }
                }
            }
            
            JSONObject orderDetail = orderService.getOrderDetail(orderId);
            return Result.success(orderDetail, "获取餐厅订单详情成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新餐厅订单状态
     */
    @RequiresPermissions("dishOrder:update")
    @PostMapping("/update-status")
    public Result updateRestaurantOrderStatus(
            @RequestBody JSONObject params,
            @RequestHeader("x-user-id") Long currentUserId) {
        try {
            // 检查用户权限
            boolean isAdmin = isAdminUser(currentUserId);
            if (!isAdmin) {
                // 非管理员用户只能更新自己餐厅的订单
                Long restaurantId = getRestaurantIdByUserId(currentUserId);
                if (restaurantId != null) {
                    Long orderId = params.getLong("orderId");
                    // 检查订单是否属于该餐厅
                    JSONObject orderDetail = orderService.getOrderDetail(orderId);
                    if (orderDetail != null) {
                        Long orderRestaurantId = orderDetail.getLong("restaurantId");
                        if (!restaurantId.equals(orderRestaurantId)) {
                            return Result.error("无权限更新该订单");
                        }
                    }
                }
            }
            
            Long orderId = params.getLong("orderId");
            Integer orderStatus = params.getInteger("orderStatus");
            orderService.updateOrderStatus(orderId, orderStatus);
            return Result.success(null, "餐厅订单状态更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 创建餐厅订单
     */
    @PostMapping("/create")
    public Result createRestaurantOrder(
            @RequestBody JSONObject params) {
        try {
            Long orderId = orderService.createOrder(params);
            return Result.success(orderId, "订单创建成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
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
    private Long getRestaurantIdByUserId(Long userId) {
        try {
            // 调用orderService的方法获取餐厅ID
            return orderService.getRestaurantIdByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}