package com.bistu.ecadmin.controller;

import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.TourCompany;
import com.bistu.ecadmin.service.TourCompanyService;
import com.bistu.ecadmin.annotation.OperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ecadmin/tour/company")
@Api(tags = "旅游公司管理")
public class TourCompanyController {

    @Autowired
    private TourCompanyService tourCompanyService;

    @GetMapping("/list")
    @ApiOperation("分页查询公司列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "公司名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "营业状态", dataType = "Integer", paramType = "query")
    })
    public Result<PageResult<TourCompany>> list(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer limit,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(required = false) Integer status) {
        return tourCompanyService.list(page, limit, name, status);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询公司详情")
    public Result<TourCompany> getById(@PathVariable Long id) {
        return Result.success(tourCompanyService.getById(id));
    }

    @RequiresPermissions("tourCompany:add")
    @PostMapping("/create")
    @ApiOperation("创建公司（商家上传默认为待审核状态）")
    @OperateLog(operation = "新增旅游公司")
    public Result<?> create(@RequestBody TourCompany company) {
        return tourCompanyService.create(company);
    }

    @RequiresPermissions("tourCompany:update")
    @PostMapping("/update")
    @ApiOperation("更新公司（商家只能更新自己的数据，且不能修改状态）")
    @OperateLog(operation = "更新旅游公司")
    public Result<?> update(@RequestBody TourCompany company) {
        return tourCompanyService.update(company);
    }

    @RequiresPermissions("tourCompany:delete")
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除公司（商家只能删除自己的数据）")
    @ApiImplicitParam(name = "id", value = "公司ID", required = true, dataType = "Long", paramType = "path")
    @OperateLog(operation = "删除旅游公司")
    public Result<?> delete(@PathVariable Long id) {
        return tourCompanyService.delete(id);
    }

    @RequiresPermissions("tourCompany:publish")
    @PostMapping("/{id}/publish")
    @ApiOperation("上架旅游公司（仅管理员，将营业状态改为1-营业中）")
    @OperateLog(operation = "上架旅游公司")
    public Result<?> publish(@PathVariable Long id) {
        try {
            boolean success = tourCompanyService.publish(id);
            return success ? Result.success("上架成功") : Result.error("上架失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @RequiresPermissions("tourCompany:unpublish")
    @PostMapping("/{id}/unpublish")
    @ApiOperation("下架旅游公司（仅管理员，将营业状态改为2-待审核）")
    @OperateLog(operation = "下架旅游公司")
    public Result<?> unpublish(@PathVariable Long id) {
        try {
            boolean success = tourCompanyService.unpublish(id);
            return success ? Result.success("下架成功") : Result.error("下架失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
