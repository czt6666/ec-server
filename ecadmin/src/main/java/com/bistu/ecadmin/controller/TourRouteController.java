package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.TourRoute;
import com.bistu.ecadmin.service.TourRouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ecadmin/tour/route")
@Api(tags = "旅游路线管理")
public class TourRouteController {

    @Autowired
    private TourRouteService tourRouteService;

    @GetMapping("/list")
    @ApiOperation("分页查询路线列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "路线名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizStatus", value = "经营状态", dataType = "Integer", paramType = "query")
    })
    public Result<PageResult<TourRoute>> list(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer limit,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(required = false) Integer bizStatus) {
        return tourRouteService.list(page, limit, name, bizStatus);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询路线详情")
    public Result<TourRoute> getById(@PathVariable Long id) {
        return Result.success(tourRouteService.getById(id));
    }

    @PostMapping("/create")
    @ApiOperation("创建路线")
    public Result<?> create(@RequestBody TourRoute route) {
        return tourRouteService.create(route);
    }

    @PostMapping("/update")
    @ApiOperation("更新路线")
    public Result<?> update(@RequestBody TourRoute route) {
        return tourRouteService.update(route);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除路线")
    @ApiImplicitParam(name = "id", value = "路线ID", required = true, dataType = "Long", paramType = "path")
    public Result<?> delete(@PathVariable Long id) {
        return tourRouteService.delete(id);
    }
}

