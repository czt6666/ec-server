package com.bistu.ecadmin.controller;

import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.ecadmin.dao.DTO.RestaurantQueryDTO;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Restaurant;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.RestaurantService;
import com.bistu.ecadmin.annotation.OperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/ecadmin/restaurant")
@Api(tags = "餐饮门店管理")
@Slf4j
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    /**
     * 管理员查看全部门店列表
     */
    @RequiresPermissions("restaurant:list")
    @GetMapping("/list")
    @ApiOperation("门店列表")
    public Result<PageResult> list(RestaurantQueryDTO dto) {
        return Result.success(restaurantService.list(dto));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取门店详情")
    public Result<Restaurant> get(@PathVariable Long id) {
        return Result.success(restaurantService.getById(id));
    }
    @RequiresPermissions("restaurant:add")
    @PostMapping("/add")
    @ApiOperation("新增门店")
    @OperateLog(operation = "新增餐饮门店")
    public Result<String> add(@RequestBody Restaurant restaurant) {
        try {
            restaurantService.create(restaurant);
            return Result.success("新增成功");
        } catch (Exception e) {
            log.error("新增门店失败", e);
            return Result.error(e.getMessage());
        }
    }
    @RequiresPermissions("restaurant:update")
    @PostMapping("/update")
    @ApiOperation("编辑门店")
    @OperateLog(operation = "更新餐饮门店")
    public Result<String> update(@RequestBody Restaurant restaurant) {
        try {
            restaurantService.update(restaurant);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("编辑门店失败", e);
            return Result.error(e.getMessage());
        }
    }
    @RequiresPermissions("restaurant:delete")
    @DeleteMapping("/{id}")
    @ApiOperation("删除门店")
    @OperateLog(operation = "删除餐饮门店")
    public Result<String> delete(@PathVariable Long id) {
        try {
            restaurantService.delete(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除门店失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 商户查看自己名下的门店名称列表
     */
    @RequiresPermissions("restaurant:list")
    @GetMapping("/user/{userId}/names")
    @ApiOperation("查询指定用户的所有饭馆名称")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    public Result<List<String>> listNames(@PathVariable Long userId) {
        return Result.success(restaurantService.listNamesByUser(userId));
    }

    @GetMapping("/name/{name}/id")
    @ApiOperation("通过餐厅名称获取餐厅ID")
    @ApiImplicitParam(name = "name", value = "餐厅名称", required = true, dataType = "String", paramType = "path")
    public Result<Long> getIdByName(@PathVariable String name) {
        try {
            Long id = restaurantService.getIdByName(name);
            if (id != null) {
                return Result.success(id);
            } else {
                return Result.error("未找到指定名称的餐厅");
            }
        } catch (Exception e) {
            log.error("通过餐厅名称获取餐厅ID失败", e);
            return Result.error("获取餐厅ID失败：" + e.getMessage());
        }
    }

    /**
     * 商户查看自己名下的门店（包含ID和名称）
     */
    @RequiresPermissions("restaurant:list")
    @GetMapping("/user/{userId}/list")
    @ApiOperation("查询指定用户的所有饭馆（包含ID和名称）")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    public Result<List<Restaurant>> listByUser(@PathVariable Long userId) {
        return Result.success(restaurantService.listByUser(userId));
    }
}