package com.bistu.ecadmin.controller;


import com.bistu.ecadmin.dao.DTO.VillageHomestayPageQueryDTO;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.VillageHomestay;
import com.bistu.ecadmin.service.VillageHomestayService;
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
        VillageHomestay homestay = villageHomestayService.getById(id, null);
        return Result.success(homestay);
    }
    
    @PostMapping
    @ApiOperation("新增民宿")
    public Result add(@RequestBody VillageHomestay homestay) {
        boolean success = villageHomestayService.add(homestay);
        return success ? Result.success() : Result.error("新增失败");
    }
    
    @PutMapping
    @ApiOperation("更新民宿")
    public Result update(@RequestBody VillageHomestay homestay) {
        boolean success = villageHomestayService.update(homestay);
        return success ? Result.success() : Result.error("更新失败");
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("删除民宿")
    public Result deleteById(@PathVariable Integer id) {
        boolean success = villageHomestayService.deleteById(id);
        return success ? Result.success() : Result.error("删除失败");
    }
    
    @GetMapping("/village/{villageId}")
    @ApiOperation("根据乡村ID查询民宿列表")
    public Result<List<VillageHomestay>> getByVillageId(@PathVariable Integer villageId) {
        List<VillageHomestay> list = villageHomestayService.getByVillageId(villageId);
        return Result.success(list);
    }
}