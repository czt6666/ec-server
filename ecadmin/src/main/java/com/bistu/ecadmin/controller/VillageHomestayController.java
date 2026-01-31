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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;

@RestController
@RequestMapping("/admin/ecadmin/village-homestay")
@Api(tags = "乡村民宿管理")
public class VillageHomestayController {

    @Autowired
    private VillageHomestayService villageHomestayService;

    /**
     * 分页查询民宿列表
     * 说明：
     * - 后端管理端登录用户：通过 TokenUtil 在 Service 层做"仅看自己"/"管理员看全部"的过滤
     * - 小程序/匿名访问：不带后端登录 token 时，自动过滤只显示已上架的民宿（status=1）
     */
    @GetMapping("/page")
    @ApiOperation("分页查询民宿列表（支持模糊查询，小程序端可访问）")
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

    /**
     * 根据ID查询民宿详情
     * 说明：小程序端可访问，会返回收藏信息
     * 将 coverImage 转换为数组格式返回（与驿站模块保持一致）
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询民宿详情（小程序端可访问）")
    public Result<Map<String, Object>> getById(@PathVariable Integer id) {
        VillageHomestay homestay = villageHomestayService.getById(id);
        
        // 转换为Map格式，将coverImage转换为数组（兼容JSON数组和逗号分隔字符串）
        Map<String, Object> result = new HashMap<>();
        result.put("id", homestay.getId());
        result.put("userId", homestay.getUserId());
        result.put("villageId", homestay.getVillageId());
        result.put("homestayName", homestay.getHomestayName());
        result.put("address", homestay.getAddress());
        result.put("status", homestay.getStatus());
        result.put("starLevel", homestay.getStarLevel());
        result.put("roomCount", homestay.getRoomCount());
        result.put("bedCount", homestay.getBedCount());
        result.put("maxCapacity", homestay.getMaxCapacity());
        result.put("contactName", homestay.getContactName());
        result.put("contactPhone", homestay.getContactPhone());
        result.put("description", homestay.getDescription());
        result.put("latitude", homestay.getLatitude());
        result.put("longitude", homestay.getLongitude());
        result.put("qualificationImages", homestay.getQualificationImages());
        result.put("linkAddress", homestay.getLinkAddress());
        result.put("miniProgramAppid", homestay.getMiniProgramAppid());
        result.put("miniProgramPath", homestay.getMiniProgramPath());
        result.put("createTime", homestay.getCreateTime());
        result.put("updateTime", homestay.getUpdateTime());
        result.put("collectNumber", homestay.getCollectNumber());
        result.put("isCollect", homestay.getIsCollect());
        result.put("userName", homestay.getUserName());
        
        // 处理封面图：转换为数组（兼容JSON数组和逗号分隔字符串）
        List<String> coverImages = new ArrayList<>();
        if (homestay.getCoverImage() != null && !homestay.getCoverImage().trim().isEmpty()) {
            String coverImageStr = homestay.getCoverImage().trim();
            try {
                // 先尝试解析为JSON数组
                Object parsed = JSON.parse(coverImageStr);
                if (parsed instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<String> jsonList = (List<String>) parsed;
                    for (String img : jsonList) {
                        if (img != null && !img.trim().isEmpty()) {
                            String trimmedImg = img.trim();
                            // 去掉/api前缀（如果有）
                            if (trimmedImg.startsWith("/api")) {
                                trimmedImg = trimmedImg.substring(4);
                            }
                            coverImages.add(trimmedImg);
                        }
                    }
                } else {
                    // 如果不是数组，当作逗号分隔字符串处理
                    String[] photos = coverImageStr.split(",");
                    for (String photo : photos) {
                        String trimmedPhoto = photo.trim();
                        if (!trimmedPhoto.isEmpty()) {
                            // 去掉/api前缀（如果有）
                            if (trimmedPhoto.startsWith("/api")) {
                                trimmedPhoto = trimmedPhoto.substring(4);
                            }
                            coverImages.add(trimmedPhoto);
                        }
                    }
                }
            } catch (Exception e) {
                // JSON解析失败，当作逗号分隔字符串处理
                String[] photos = coverImageStr.split(",");
                for (String photo : photos) {
                    String trimmedPhoto = photo.trim();
                    if (!trimmedPhoto.isEmpty()) {
                        // 去掉/api前缀（如果有）
                        if (trimmedPhoto.startsWith("/api")) {
                            trimmedPhoto = trimmedPhoto.substring(4);
                        }
                        coverImages.add(trimmedPhoto);
                    }
                }
            }
        }
        result.put("coverImage", coverImages);
        
        return Result.success(result);
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

    /**
     * 根据乡村ID查询民宿列表
     * 说明：
     * - 后端管理端登录用户：可查看该乡村下的所有民宿（包括待审核、已下架等）
     * - 小程序/匿名访问：自动过滤只显示已上架的民宿（status=1），并返回收藏信息
     */
    @GetMapping("/village/{villageId}")
    @ApiOperation("根据乡村ID查询民宿列表（小程序端可访问）")
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