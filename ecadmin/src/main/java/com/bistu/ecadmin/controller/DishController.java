package com.bistu.ecadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.dao.DTO.SortRequest;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bistu.ecadmin.annotation.OperateLog;

import java.util.List;

/**
 * 菜品Controller
 */
@RestController
@RequestMapping("/restaurant/dish")
@Api(tags = "菜品管理")
public class DishController {
    
    @Autowired
    private DishService dishService;
    
    @PostMapping("/add")
    @ApiOperation("新增菜品")
    @OperateLog(operation = "新增商品菜品")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "restaurantId", value = "餐厅ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "categoryId", value = "菜品分类ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "dishName", value = "菜品名称", required = true, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "price", value = "菜品价格", required = true, dataType = "Double", paramType = "body"),
        @ApiImplicitParam(name = "unit", value = "单位", required = false, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "status", value = "菜品状态", required = false, dataType = "Integer", paramType = "body"),
        @ApiImplicitParam(name = "description", value = "菜品描述", required = false, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "body")
    })
    public Result<?> addDish(@RequestBody JSONObject params) {
        return dishService.addDish(params);
    }
    
    @PostMapping("/update")
    @ApiOperation("更新菜品")
    @OperateLog(operation = "更新商品菜品")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "菜品ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "restaurantId", value = "餐厅ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "categoryId", value = "菜品分类ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "dishName", value = "菜品名称", required = true, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "price", value = "菜品价格", required = true, dataType = "Double", paramType = "body"),
        @ApiImplicitParam(name = "unit", value = "单位", required = false, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "status", value = "菜品状态", required = false, dataType = "Integer", paramType = "body"),
        @ApiImplicitParam(name = "description", value = "菜品描述", required = false, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "body")
    })
    public Result<?> updateDish(@RequestBody JSONObject params) {
        return dishService.updateDish(params);
    }
    
    @GetMapping("/list")
    @ApiOperation("分页查询菜品列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页大小", defaultValue = "10", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "restaurantName", value = "餐厅名称", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "categoryName", value = "分类名称", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "dishName", value = "菜品名称", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "status", value = "菜品状态", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "userId", value = "用户ID", required = false, dataType = "Long", paramType = "query")
    })
    public Result<?> listDishes(@RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer pageSize,
                                @RequestParam(required = false) String restaurantName,
                                @RequestParam(required = false) String categoryName,
                                @RequestParam(required = false) String dishName,
                                @RequestParam(required = false) Integer status,
                                @RequestParam(required = false) Long userId) {
        JSONObject params = new JSONObject();
        params.put("page", page);
        params.put("pageSize", pageSize);
        params.put("restaurantName", restaurantName);
        params.put("categoryName", categoryName);
        params.put("dishName", dishName);
        params.put("status", status);
        params.put("userId", userId);
        
        return dishService.listDishes(params);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取菜品详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "菜品ID", required = true, dataType = "Long", paramType = "path"),
        @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "query")
    })
    public Result<?> getDishDetail(@PathVariable Long id, @RequestParam Long userId) {
        JSONObject params = new JSONObject();
        params.put("id", id);
        params.put("userId", userId);
        
        return dishService.getDishDetail(params);
    }
    
    @PostMapping("/delete")
    @ApiOperation("删除菜品")
    @OperateLog(operation = "删除商品菜品")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "菜品ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "body")
    })
    public Result<?> deleteDish(@RequestBody JSONObject params) {
        return dishService.deleteDish(params);
    }
    
    @PostMapping("/updateSort")
    @ApiOperation("更新菜品排序")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sortRequests", value = "排序请求列表", required = true, dataType = "List", paramType = "body")
    })
    public Result<?> updateDishSort(@RequestBody List<SortRequest> sortRequests) {
        try {
            // 遍历排序请求列表
            for (SortRequest request : sortRequests) {
                // 根据ID更新菜品的sortNum字段
                dishService.updateSortNum(request.getId(), request.getSortNum());
            }
            
            return Result.success("排序更新成功");
        } catch (Exception e) {
            return Result.error("排序更新失败: " + e.getMessage());
        }
    }
}