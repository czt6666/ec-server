package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.UserCollect;
import com.bistu.ecadmin.service.UserCollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ecadmin/collect")
@Api(tags = "用户收藏管理")
public class UserCollectController {

    @Autowired
    private UserCollectService userCollectService;

    @GetMapping("/list")
    @ApiOperation("分页查询收藏列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "targetType", value = "收藏对象类型 plan/activity", dataType = "String", paramType = "query")
    })
    public Result<PageResult<UserCollect>> list(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer limit,
                                                @RequestParam(required = false) Long userId,
                                                @RequestParam(required = false) String targetType) {
        return userCollectService.list(page, limit, userId, targetType);
    }

    @PostMapping("/create")
    @ApiOperation("新增收藏")
    public Result<?> create(@RequestBody UserCollect collect) {
        return userCollectService.create(collect);
    }

    @GetMapping("/get")
    @ApiOperation("查询是否收藏（按userId+targetType+targetId）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "targetType", value = "收藏对象类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "targetId", value = "收藏对象ID", required = true, dataType = "String", paramType = "query")
    })
    public Result<UserCollect> get(@RequestParam Long userId,
                                   @RequestParam String targetType,
                                   @RequestParam String targetId) {
        return userCollectService.get(userId, targetType, targetId);
    }

    @DeleteMapping("/delete")
    @ApiOperation("取消收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "targetType", value = "收藏对象类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "targetId", value = "收藏对象ID", required = true, dataType = "String", paramType = "query")
    })
    public Result<?> delete(@RequestParam Long userId,
                            @RequestParam String targetType,
                            @RequestParam String targetId) {
        return userCollectService.delete(userId, targetType, targetId);
    }

    @GetMapping("/count")
    @ApiOperation("按对象统计收藏数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "targetType", value = "收藏对象类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "targetId", value = "收藏对象ID", required = true, dataType = "String", paramType = "query")
    })
    public Result<Integer> countByTarget(@RequestParam String targetType,
                                         @RequestParam String targetId) {
        return userCollectService.countByTarget(targetType, targetId);
    }

    @GetMapping("/hotspot")
    @ApiOperation("收藏热点榜（按收藏人数降序，可限定最近N天）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "targetType", value = "收藏对象类型（可选，不传则查询所有类型）", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "days", value = "限定最近N天（可选，默认7）", dataType = "Integer", paramType = "query")
    })
    public Result<PageResult<com.bistu.ecadmin.pojo.UserCollectHotspot>> hotspot(@RequestParam(defaultValue = "1") Integer page,
                                                                                 @RequestParam(defaultValue = "10") Integer limit,
                                                                                 @RequestParam(required = false) String targetType,
                                                                                 @RequestParam(required = false) Integer days) {
        Integer d = (days == null || days <= 0) ? 7 : days;
        return userCollectService.hotspot(page, limit, targetType, d);
    }
}

