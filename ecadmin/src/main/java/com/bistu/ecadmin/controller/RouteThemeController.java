package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.RouteTheme;
import com.bistu.ecadmin.service.RouteThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ecadmin/route/theme")
@Api(tags = "线路主题管理")
public class RouteThemeController {

    @Autowired
    private RouteThemeService routeThemeService;

    @PostMapping("/create")
    @ApiOperation("创建线路主题")
    public Result<?> create(@RequestBody RouteTheme routeTheme) {
        return routeThemeService.create(routeTheme);
    }

    @PostMapping("/update")
    @ApiOperation("更新线路主题")
    public Result<?> update(@RequestBody RouteTheme routeTheme) {
        return routeThemeService.update(routeTheme);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除线路主题")
    @ApiImplicitParam(name = "id", value = "主题ID", required = true, dataType = "Long", paramType = "path")
    public Result<?> delete(@PathVariable Long id) {
        return routeThemeService.delete(id);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询线路主题列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "themeName", value = "主题名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "Integer", paramType = "query")
    })
    public Result<PageResult<RouteTheme>> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                               @RequestParam(required = false, defaultValue = "10") Integer limit,
                                               @RequestParam(required = false) String themeName,
                                               @RequestParam(required = false) Integer status) {
        return routeThemeService.list(page, limit, themeName, status);
    }
}




