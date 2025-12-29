package com.bistu.ecadmin.controller;

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

    @PostMapping("/create")
    @ApiOperation("创建公司")
    @OperateLog(operation = "新增旅游公司")
    public Result<?> create(@RequestBody TourCompany company) {
        return tourCompanyService.create(company);
    }

    @PostMapping("/update")
    @ApiOperation("更新公司")
    @OperateLog(operation = "更新旅游公司")
    public Result<?> update(@RequestBody TourCompany company) {
        return tourCompanyService.update(company);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除公司")
    @ApiImplicitParam(name = "id", value = "公司ID", required = true, dataType = "Long", paramType = "path")
    @OperateLog(operation = "删除旅游公司")
    public Result<?> delete(@PathVariable Long id) {
        return tourCompanyService.delete(id);
    }
}