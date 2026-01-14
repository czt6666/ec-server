package com.bistu.ecadmin.controller;

import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.TourRoute;
import com.bistu.ecadmin.service.TourRouteService;
import com.bistu.ecadmin.annotation.OperateLog;
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

    /**
     * 分页查询旅游路线
     * 说明：
     * - 管理后台用户：依旧需要在 Service 层基于角色判断“管理员看全部 / 商家只看自己”
     * - 小程序/匿名访问：不带后台 token 时允许访问，Service 层会自动只返回 status=1 的线路并附带收藏信息
     */
    @GetMapping("/list")
    @ApiOperation("分页查询路线列表（小程序可访问）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "路线名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizStatus", value = "经营状态", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "companyId", value = "公司ID", dataType = "Long", paramType = "query")
    })
    public Result<PageResult<TourRoute>> list(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer limit,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(required = false) Integer bizStatus,
                                              @RequestParam(required = false) Long companyId) {
        return tourRouteService.list(page, limit, name, bizStatus, companyId);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询路线详情")
    public Result<TourRoute> getById(@PathVariable Long id) {
        return Result.success(tourRouteService.getById(id));
    }

    @RequiresPermissions("tourRoute:add")
    @PostMapping("/create")
    @ApiOperation("创建路线（商家只能在自己公司下创建）")
    @OperateLog(operation = "新增旅游路线")
    public Result<?> create(@RequestBody TourRoute route) {
        return tourRouteService.create(route);
    }

    @RequiresPermissions("tourRoute:update")
    @PostMapping("/update")
    @ApiOperation("更新路线（商家只能更新自己公司下的路线）")
    @OperateLog(operation = "更新旅游路线")
    public Result<?> update(@RequestBody TourRoute route) {
        return tourRouteService.update(route);
    }

    @RequiresPermissions("tourRoute:delete")
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除路线（商家只能删除自己公司下的路线）")
    @ApiImplicitParam(name = "id", value = "路线ID", required = true, dataType = "Long", paramType = "path")
    @OperateLog(operation = "删除旅游路线")
    public Result<?> delete(@PathVariable Long id) {
        return tourRouteService.delete(id);
    }
}
