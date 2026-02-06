package com.bistu.ecadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.OrderService;
import com.bistu.ecadmin.util.JwtUtil;
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

    @GetMapping("/list")
    public Result getRestaurantOrderList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageRow,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(required = false) Integer orderStatus,
            @RequestHeader(value = "X-User-Id", required = true) String xUserId) {
        
        JSONObject params = new JSONObject();
        params.put("pageNum", pageNum);
        params.put("pageRow", pageRow);
        if (orderNo != null) params.put("orderNo", orderNo);
        if (userId != null) params.put("userId", userId);
        if (restaurantId != null) params.put("restaurantId", restaurantId);
        if (orderStatus != null) params.put("orderStatus", orderStatus);

        try {
            // 从X-User-Id请求头获取currentUserId
            Long currentUserId = Long.parseLong(xUserId.trim());
            
            // 将currentUserId放入params
            params.put("currentUserId", currentUserId);
            
            JSONObject result = orderService.getOrderList(params);
            return Result.success(result, "获取餐厅订单列表成功");
        } catch (NumberFormatException e) {
            return Result.error("X-User-Id格式错误");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取餐厅订单详情
     */
    @GetMapping("/detail/{orderId}")
    public Result getRestaurantOrderDetail(
            @PathVariable Long orderId) {
        try {
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
            @RequestBody JSONObject params) {
        try {
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
            @RequestBody JSONObject params,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        try {
            // 解析Authorization头获取userId
            Long userId = null;
            if (authorizationHeader != null && !authorizationHeader.trim().isEmpty()) {
                String token = authorizationHeader.trim();
                
                // 去除Bearer前缀
                if (token.startsWith("Bearer ")) {
                    token = token.substring(7).trim();
                }
                
                // 去除可能的花括号和引号
                token = token.replaceAll("^[{\"']+", "").replaceAll("[}\"']+$", "");
                token = token.trim();
                
                // 从token中获取userId
                userId = JwtUtil.getUserIdFromToken(token);
                if (userId == null || !JwtUtil.validateToken(token)) {
                    return Result.error("无效的令牌");
                }
            }
            
            if (userId == null) {
                return Result.error("未提供有效的用户令牌");
            }
            
            // 将解析得到的userId放入params
            params.put("userId", userId);
            
            Long orderId = orderService.createOrder(params);
            return Result.success(orderId, "订单创建成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据小程序ID获取用户订单
     */
    @GetMapping("/mini-program/list")
    public Result getMiniProgramUserOrders(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageRow,
            @RequestParam(required = false) Integer orderStatus,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        try {
            // 解析Authorization头获取userId
            Long userId = null;
            if (authorizationHeader != null && !authorizationHeader.trim().isEmpty()) {
                String token = authorizationHeader.trim();
                
                // 去除Bearer前缀
                if (token.startsWith("Bearer ")) {
                    token = token.substring(7).trim();
                }
                
                // 去除可能的花括号和引号
                token = token.replaceAll("^[{\"']+", "").replaceAll("[}\"']+$", "");
                token = token.trim();
                
                // 从token中获取userId
                userId = JwtUtil.getUserIdFromToken(token);
                if (userId == null || !JwtUtil.validateToken(token)) {
                    return Result.error("无效的令牌");
                }
            }
            
            if (userId == null) {
                return Result.error("未提供有效的用户令牌");
            }
            
            // 构建查询参数
            JSONObject params = new JSONObject();
            params.put("pageNum", pageNum);
            params.put("pageRow", pageRow);
            params.put("userId", userId);
            if (orderStatus != null) {
                params.put("orderStatus", orderStatus);
            }
            params.put("currentUserId", userId); // 用于权限验证
            
            // 获取订单列表
            JSONObject result = orderService.getOrderList(params);
            return Result.success(result, "获取用户订单列表成功");
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