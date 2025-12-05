package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.dao.DTO.StationPageQueryDTO;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.Station;
import com.bistu.ecadmin.service.StationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ecadmin/station")
@Api(tags = "驿站主体信息管理")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping("/page")
    @ApiOperation("分页查询驿站列表（支持模糊查询）")
    public Result<PageResult> page(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) Integer status,
                                    @RequestParam(required = false) String keyword) {
        StationPageQueryDTO dto = new StationPageQueryDTO();
        dto.setPage(page);
        dto.setPageSize(pageSize);
        dto.setName(name);
        dto.setStatus(status);
        dto.setKeyword(keyword);
        return Result.success(stationService.page(dto));
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询驿站详情")
    public Result<Station> getById(@PathVariable Long id) {
        Station station = stationService.getById(id);
        return Result.success(station);
    }

    @PostMapping("/add")
    @ApiOperation("新增驿站")
    public Result add(@RequestBody Station station) {
        boolean success = stationService.add(station);
        return success ? Result.success() : Result.error("新增失败");
    }

    @PutMapping("/update")
    @ApiOperation("更新驿站")
    public Result update(@RequestBody Station station) {
        boolean success = stationService.update(station);
        return success ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping("/deleted/{id}")
    @ApiOperation("删除驿站")
    public Result deleteById(@PathVariable Long id) {
        boolean success = stationService.deleteById(id);
        return success ? Result.success() : Result.error("删除失败");
    }
}

