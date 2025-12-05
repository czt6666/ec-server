package com.bistu.ecadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.pojo.ElderlyServiceMode;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.ElderlyServiceModeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 养老服务模式Controller
 */
@RestController
@RequestMapping("/elderly/service/mode")
@Api(tags = "养老服务模式管理")
public class ElderlyServiceModeController {
    
    @Autowired
    private ElderlyServiceModeService elderlyServiceModeService;
    
    @PostMapping("/create")
    @ApiOperation("创建养老服务模式")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "modeName", value = "服务模式名称", required = true, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "Integer", paramType = "body"),
        @ApiImplicitParam(name = "status", value = "状态（1：启用；0：禁用）", required = false, dataType = "Integer", paramType = "body")
    })
    public Result<?> createServiceMode(@RequestBody ElderlyServiceMode elderlyServiceMode) {
        return elderlyServiceModeService.createServiceMode(elderlyServiceMode);
    }
    
    @GetMapping("/list")
    @ApiOperation("获取养老服务模式列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "modeName", value = "服务模式名称", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "status", value = "状态", dataType = "Integer", paramType = "query")
    })
    public Result<PageResult> listServiceModes(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) String modeName,
            @RequestParam(required = false) Integer status
    ) {
        return elderlyServiceModeService.listServiceModes(page, limit, modeName, status);
    }
    
    @PostMapping("/update")
    @ApiOperation("更新养老服务模式")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "服务模式ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "modeName", value = "服务模式名称", required = false, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "Integer", paramType = "body"),
        @ApiImplicitParam(name = "status", value = "状态（1：启用；0：禁用）", required = false, dataType = "Integer", paramType = "body")
    })
    public Result<?> updateServiceMode(@RequestBody ElderlyServiceMode elderlyServiceMode) {
        return elderlyServiceModeService.updateServiceMode(elderlyServiceMode);
    }
    
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除养老服务模式")
    @ApiImplicitParam(name = "id", value = "服务模式ID", required = true, dataType = "Long", paramType = "path")
    public Result<?> deleteServiceMode(@PathVariable Long id) {
        return elderlyServiceModeService.deleteServiceMode(id);
    }
    
    @PostMapping("/status/update")
    @ApiOperation("更新养老服务模式状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "服务模式ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "status", value = "状态（1：启用；0：禁用）", required = true, dataType = "Integer", paramType = "body")
    })
    public Result<?> updateStatus(@RequestBody JSONObject params) {
        Long id = params.getLong("id");
        Integer status = params.getInteger("status");
        return elderlyServiceModeService.updateStatus(id, status);
    }
}