package com.bistu.ecadmin.controller;


import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.ecadmin.dao.DTO.VillageHomestayPageQueryDTO;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.VillageHomestay;
import com.bistu.ecadmin.service.VillageHomestayService;
import com.bistu.ecadmin.annotation.OperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/ecadmin/village-homestay")
@Api(tags = "乡村民宿管理")
public class VillageHomestayController {

    @Autowired
    private VillageHomestayService villageHomestayService;

    @RequiresPermissions("villageHomestay:list")
    @GetMapping("/page")
    @ApiOperation("分页查询民宿列表（支持模糊查询）")
    public Result<PageResult> page(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(required = false) Integer villageId,
                                   @RequestParam(required = false) String homestayName,
                                   @RequestParam(required = false) String address,
                                   @RequestParam(required = false) Integer status,
                                   @RequestParam(required = false) Integer starLevel,
                                   @RequestParam(required = false) String contactName,
                                   @RequestParam(required = false) String keyword) {
        VillageHomestayPageQueryDTO dto = new VillageHomestayPageQueryDTO();
        dto.setPage(page);
        dto.setPageSize(pageSize);
        dto.setVillageId(villageId);
        dto.setHomestayName(homestayName);
        dto.setAddress(address);
        dto.setStatus(status);
        dto.setStarLevel(starLevel);
        dto.setContactName(contactName);
        dto.setKeyword(keyword);
        return Result.success(villageHomestayService.page(dto));
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询民宿详情")
    public Result<VillageHomestay> getById(@PathVariable Integer id) {
        VillageHomestay homestay = villageHomestayService.getById(id);
        return Result.success(homestay);
    }

    @RequiresPermissions("villageHomestay:add")
    @PostMapping
    @ApiOperation("新增民宿（商家上传默认为待审核状态）")
    @OperateLog(operation = "新增乡村民宿")
    public Result add(@RequestBody VillageHomestay homestay) {
        try {
            boolean success = villageHomestayService.add(homestay);
            return success ? Result.success() : Result.error("新增失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @RequiresPermissions("villageHomestay:update")
    @PutMapping
    @ApiOperation("更新民宿（商家只能更新自己的数据，且不能修改状态）")
    @OperateLog(operation = "更新乡村民宿")
    public Result update(@RequestBody VillageHomestay homestay) {
        try {
            boolean success = villageHomestayService.update(homestay);
            return success ? Result.success() : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @RequiresPermissions("villageHomestay:delete")
    @DeleteMapping("/{id}")
    @ApiOperation("删除民宿（商家只能删除自己的数据）")
    @OperateLog(operation = "删除乡村民宿")
    public Result deleteById(@PathVariable Integer id) {
        try {
            boolean success = villageHomestayService.deleteById(id);
            return success ? Result.success() : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/village/{villageId}")
    @ApiOperation("根据乡村ID查询民宿列表")
    public Result<List<VillageHomestay>> getByVillageId(@PathVariable Integer villageId) {
        List<VillageHomestay> list = villageHomestayService.getByVillageId(villageId);
        return Result.success(list);
    }

    @RequiresPermissions("villageHomestay:publish")
    @PostMapping("/{id}/publish")
    @ApiOperation("上架民宿（仅管理员，将状态改为1-营业）")
    @OperateLog(operation = "上架乡村民宿")
    public Result publish(@PathVariable Integer id) {
        try {
            boolean success = villageHomestayService.publish(id);
            return success ? Result.success("上架成功") : Result.error("上架失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @RequiresPermissions("villageHomestay:unpublish")
    @PostMapping("/{id}/unpublish")
    @ApiOperation("下架民宿（仅管理员，将状态改为3-已下架）")
    @OperateLog(operation = "下架乡村民宿")
    public Result unpublish(@PathVariable Integer id) {
        try {
            boolean success = villageHomestayService.unpublish(id);
            return success ? Result.success("下架成功") : Result.error("下架失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}