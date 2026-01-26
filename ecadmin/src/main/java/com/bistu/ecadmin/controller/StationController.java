package com.bistu.ecadmin.controller;

import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.ecadmin.dao.DTO.StationPageQueryDTO;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.Station;
import com.bistu.ecadmin.service.StationService;
import com.bistu.ecadmin.annotation.OperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/ecadmin/station")
@Api(tags = "驿站主体信息管理")
@Slf4j
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
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        Station station = stationService.getById(id);

        // 转换为Map格式，将environmentPhotos转换为数组并去掉/api前缀
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("id", station.getId());
        result.put("name", station.getName());
        result.put("registeredAddress", station.getRegisteredAddress());
        result.put("businessAddress", station.getBusinessAddress());
        result.put("registeredLatitude", station.getRegisteredLatitude());
        result.put("registeredLongitude", station.getRegisteredLongitude());
        result.put("businessLatitude", station.getBusinessLatitude());
        result.put("businessLongitude", station.getBusinessLongitude());
        result.put("unifiedSocialCreditCode", station.getUnifiedSocialCreditCode());
        result.put("legalRepresentative", station.getLegalRepresentative());
        result.put("registeredCapital", station.getRegisteredCapital());
        result.put("establishmentDate", station.getEstablishmentDate());
        result.put("businessTerm", station.getBusinessTerm());
        result.put("officialPhone", station.getOfficialPhone());
        result.put("emergencyContact", station.getEmergencyContact());
        result.put("emergencyPhone", station.getEmergencyPhone());
        result.put("officialEmail", station.getOfficialEmail());
        result.put("subjectTypeId", station.getSubjectTypeId());
        result.put("serviceMode", station.getServiceMode());
        result.put("elderlyLicenseNo", station.getElderlyLicenseNo());
        result.put("medicalLicenseNo", station.getMedicalLicenseNo());
        result.put("foodLicenseNo", station.getFoodLicenseNo());
        result.put("fireAcceptanceNo", station.getFireAcceptanceNo());
        result.put("businessStatus", station.getBusinessStatus());
        result.put("totalBeds", station.getTotalBeds());
        result.put("roomConfig", station.getRoomConfig());
        result.put("careLevel", station.getCareLevel());
        result.put("priceRange", station.getPriceRange());
        result.put("introduction", station.getIntroduction());

        // 处理环境照片：转换为数组并去掉/api前缀
        List<String> environmentPhotos = new ArrayList<>();
        if (station.getEnvironmentPhotos() != null && !station.getEnvironmentPhotos().trim().isEmpty()) {
            String[] photos = station.getEnvironmentPhotos().split(",");
            for (String photo : photos) {
                String trimmedPhoto = photo.trim();
                if (!trimmedPhoto.isEmpty()) {
                    // 去掉/api前缀
                    if (trimmedPhoto.startsWith("/api")) {
                        trimmedPhoto = trimmedPhoto.substring(4);
                    }
                    environmentPhotos.add(trimmedPhoto);
                }
            }
        }
        result.put("environmentPhotos", environmentPhotos);

        result.put("createTime", station.getCreateTime());
        result.put("updateTime", station.getUpdateTime());
        result.put("collectNumber", station.getCollectNumber());
        result.put("isCollect", station.getIsCollect());

        return Result.success(result);
    }

    @PostMapping("/add")
    @ApiOperation("新增驿站")
    @OperateLog(operation = "新增养老驿站")
    @RequiresPermissions("station:add")
    public Result add(@RequestBody Station station) {
        try {
            boolean success = stationService.add(station);
            return success ? Result.success() : Result.error("新增失败");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("新增驿站失败", e);
            return Result.error("新增失败：" + e.getMessage());
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新驿站")
    @OperateLog(operation = "更新养老驿站")
    @RequiresPermissions("station:update")
    public Result update(@RequestBody Station station) {
        try {
            boolean success = stationService.update(station);
            return success ? Result.success() : Result.error("更新失败");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("更新驿站失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/deleted/{id}")
    @ApiOperation("删除驿站")
    @OperateLog(operation = "删除养老驿站")
    @RequiresPermissions("station:delete")
    public Result deleteById(@PathVariable Long id) {
        boolean success = stationService.deleteById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 导入驿站信息
     */
    @PostMapping("/import")
    @ApiOperation("导入驿站信息")
    public Result<Map<String, Object>> importStations(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = stationService.importStations(file);
            return Result.success(result);
        } catch (Exception e) {
            log.error("导入驿站失败", e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出驿站信息
     */
    @GetMapping("/export")
    @ApiOperation("导出驿站信息")
    public ResponseEntity<Resource> exportStations() {
        try {
            log.info("开始导出驿站信息");
            Resource resource = stationService.exportStations();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"驿站信息.xlsx\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("导出驿站失败", e);
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    @RequiresPermissions("station:publish")
    @PostMapping("/{id}/publish")
    @ApiOperation("上架驿站（仅管理员，将营业状态改为1-营业中）")
    @OperateLog(operation = "上架养老驿站")
    public Result publish(@PathVariable Long id) {
        try {
            boolean success = stationService.publish(id);
            return success ? Result.success("上架成功") : Result.error("上架失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @RequiresPermissions("station:unpublish")
    @PostMapping("/{id}/unpublish")
    @ApiOperation("下架驿站（仅管理员，将营业状态改为3-已注销）")
    @OperateLog(operation = "下架养老驿站")
    public Result unpublish(@PathVariable Long id) {
        try {
            boolean success = stationService.unpublish(id);
            return success ? Result.success("下架成功") : Result.error("下架失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
