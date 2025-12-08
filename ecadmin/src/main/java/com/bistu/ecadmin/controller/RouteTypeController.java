package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.RouteType;
import com.bistu.ecadmin.service.RouteTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ecadmin/route/type")
@Api(tags = "线路类型管理")
public class RouteTypeController {

    @Autowired
    private RouteTypeService routeTypeService;

    @PostMapping("/create")
    @ApiOperation("创建线路类型")
    public Result<?> create(@RequestBody RouteType routeType) {
        return routeTypeService.create(routeType);
    }

    @PostMapping("/update")
    @ApiOperation("更新线路类型")
    public Result<?> update(@RequestBody RouteType routeType) {
        return routeTypeService.update(routeType);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除线路类型")
    @ApiImplicitParam(name = "id", value = "类型ID", required = true, dataType = "Long", paramType = "path")
    public Result<?> delete(@PathVariable Long id) {
        return routeTypeService.delete(id);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询线路类型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "typeName", value = "类型名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "Integer", paramType = "query")
    })
    public Result<PageResult<RouteType>> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "10") Integer limit,
                                              @RequestParam(required = false) String typeName,
                                              @RequestParam(required = false) Integer status) {
        return routeTypeService.list(page, limit, typeName, status);
    }
}



