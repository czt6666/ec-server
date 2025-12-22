package com.bistu.ecadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.ecadmin.dao.DTO.SortRequest;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.DishCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品分类Controller
 */
@RestController
@RequestMapping("/restaurant/dishCategory")
@Api(tags = "菜品分类管理")
public class DishCategoryController {

    @Autowired
    private DishCategoryService dishCategoryService;

    @PostMapping("/add")
    @ApiOperation("新增菜品分类")
    @RequiresPermissions("dishCategory:add")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryName", value = "菜品分类", required = true, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "restaurantId", value = "餐厅ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "sortNum", value = "排序号", required = false, dataType = "Integer", paramType = "body"),
        @ApiImplicitParam(name = "imageUrl", value = "分类图片URL", required = false, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "body")
    })
    public Result<?> addDishCategory(@RequestBody JSONObject params) {
        return dishCategoryService.addDishCategory(params);
    }

    @GetMapping("/list")
    @ApiOperation("获取菜品分类列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "categoryName", value = "菜品分类", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "restaurantName", value = "餐厅名称", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "status", value = "状态", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", paramType = "query")
    })
    public Result<PageResult> listDishCategories(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String restaurantName,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long userId
    ) {
        return dishCategoryService.listDishCategories(page, pageSize, categoryName, restaurantName, status, userId);
    }

    @PostMapping("/update")
    @ApiOperation("更新菜品分类")
    @RequiresPermissions("dishCategory:update")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "分类ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "categoryName", value = "菜品分类", required = false, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "restaurantName", value = "餐厅名称", required = false, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "imageUrl", value = "分类图片URL", required = false, dataType = "String", paramType = "body"),
        // 移除这行，因为实体类中没有status字段
        // @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "Integer", paramType = "body"),
        @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "body")
    })
    public Result<?> updateDishCategory(@RequestBody JSONObject params) {
        return dishCategoryService.updateDishCategory(params);
    }

    @PostMapping("/delete")
    @ApiOperation("删除菜品分类")
    @RequiresPermissions("dishCategory:delete")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "分类ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "body")
    })
    public Result<?> deleteDishCategory(@RequestBody JSONObject params) {
        return dishCategoryService.deleteDishCategory(params);
    }

    @PostMapping("/updateSort")
    @ApiOperation("更新菜品分类排序")
    @RequiresPermissions("dishCategory:update")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sortRequests", value = "排序请求列表", required = true, dataType = "List", paramType = "body")
    })
    public Result<?> updateDishCategorySort(@RequestBody List<SortRequest> sortRequests) {
        try {
            // 遍历排序请求列表
            for (SortRequest request : sortRequests) {
                // 根据ID更新菜品分类的sortNum字段
                dishCategoryService.updateSortNum(request.getId(), request.getSortNum());
            }

            return Result.success("排序更新成功");
        } catch (Exception e) {
            return Result.error("排序更新失败: " + e.getMessage());
        }
    }
}
