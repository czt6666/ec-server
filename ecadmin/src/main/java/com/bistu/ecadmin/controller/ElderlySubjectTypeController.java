package com.bistu.ecadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.pojo.ElderlySubjectType;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.ElderlySubjectTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 养老服务主体类型Controller
 */
@RestController
@RequestMapping("/elderly/subject/type")
@Api(tags = "养老服务主体类型管理")
public class ElderlySubjectTypeController {
    
    @Autowired
    private ElderlySubjectTypeService elderlySubjectTypeService;
    
    @PostMapping("/create")
    @ApiOperation("创建养老服务主体类型")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "typeName", value = "主体类型名称", required = true, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "Integer", paramType = "body"),
        @ApiImplicitParam(name = "status", value = "状态（1：启用；0：禁用）", required = false, dataType = "Integer", paramType = "body")
    })
    public Result<?> createSubjectType(@RequestBody ElderlySubjectType elderlySubjectType) {
        return elderlySubjectTypeService.createSubjectType(elderlySubjectType);
    }
    
    @GetMapping("/list")
    @ApiOperation("获取养老服务主体类型列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "typeName", value = "主体类型名称", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "status", value = "状态", dataType = "Integer", paramType = "query")
    })
    public Result<PageResult> listSubjectTypes(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) Integer status
    ) {
        return elderlySubjectTypeService.listSubjectTypes(page, limit, typeName, status);
    }
    
    @PostMapping("/update")
    @ApiOperation("更新养老服务主体类型")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "主体类型ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "typeName", value = "主体类型名称", required = false, dataType = "String", paramType = "body"),
        @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "Integer", paramType = "body"),
        @ApiImplicitParam(name = "status", value = "状态（1：启用；0：禁用）", required = false, dataType = "Integer", paramType = "body")
    })
    public Result<?> updateSubjectType(@RequestBody ElderlySubjectType elderlySubjectType) {
        return elderlySubjectTypeService.updateSubjectType(elderlySubjectType);
    }
    
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除养老服务主体类型")
    @ApiImplicitParam(name = "id", value = "主体类型ID", required = true, dataType = "Long", paramType = "path")
    public Result<?> deleteSubjectType(@PathVariable Long id) {
        return elderlySubjectTypeService.deleteSubjectType(id);
    }
    
    @PostMapping("/status/update")
    @ApiOperation("更新养老服务主体类型状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "主体类型ID", required = true, dataType = "Long", paramType = "body"),
        @ApiImplicitParam(name = "status", value = "状态（1：启用；0：禁用）", required = true, dataType = "Integer", paramType = "body")
    })
    public Result<?> updateStatus(@RequestBody JSONObject params) {
        Long id = params.getLong("id");
        Integer status = params.getInteger("status");
        return elderlySubjectTypeService.updateStatus(id, status);
    }
}