package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.dao.DTO.VillagePageQueryDTO;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.village;
import com.bistu.ecadmin.service.impl.village_serviceIml;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "村庄相关接口")
@RequestMapping("/admin/ecadmin/village")

public class village_controller {
    @Autowired
    private village_serviceIml villageServiceIml;
    
    @GetMapping("/list")
    @ApiOperation("按条件简单列表（不分页）")
    public Result village_list(village v){
        List<village> list = villageServiceIml.list(v);
        return Result.success(list);
    }
    
    @ApiOperation("分页查询村庄")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页数量", defaultValue = "5", dataType = "Integer", paramType = "query")
    })
    @GetMapping("/page")
    public Result<PageResult> page(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "5") Integer pageSize) {
        VillagePageQueryDTO dto = new VillagePageQueryDTO();
        dto.setPage(page);
        dto.setPageSize(pageSize);
        // 其他条件不再接收，默认不填
        return Result.success(villageServiceIml.page(dto));
    }
    
    /**
     * 新增村庄
     */
    @ApiOperation("增加村庄村庄")
    @PostMapping("/add")
    public Result<String> add(@RequestBody village village) {
        try {
            villageServiceIml.add(village);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.error("添加失败：" + e.getMessage());
        }
    }
    
    // 在 VillageController 中添加
    /**
     * 修改村庄
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody village village) {
        try {
            villageServiceIml.update(village);
            return Result.success("修改成功");
        } catch (Exception e) {
            return Result.error("修改失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询村庄详情
     */
    @GetMapping("/{id}")
    public Result<village> getById(@PathVariable Integer id) {
        try {
            village village = villageServiceIml.getById(id);
            return Result.success(village);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除村庄（带级联删除）
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Integer id) {
        try {
            // 先检查删除约束
            Map<String, Object> constraintResult = villageServiceIml.checkDeleteConstraints(id);
            Boolean canDelete = (Boolean) constraintResult.get("canDelete");

            if (!canDelete) {
                String message = (String) constraintResult.get("message");
                return Result.error("删除失败：" + message);
            }

            // 执行级联删除
            villageServiceIml.deleteWithCascade(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除村庄失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 强制删除村庄（忽略约束）
     */
    @DeleteMapping("/force/{id}")
    @ApiOperation("强制删除村庄")
    public Result<String> forceDelete(@PathVariable Integer id) {
        try {
            villageServiceIml.deleteWithCascade(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("强制删除村庄失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 检查删除约束
     */
    @GetMapping("/check-delete/{id}")
    @ApiOperation("检查删除约束")
    public Result<Map<String, Object>> checkDeleteConstraints(@PathVariable Integer id) {
        try {
            Map<String, Object> result = villageServiceIml.checkDeleteConstraints(id);
            return Result.success(result);
        } catch (Exception e) {
            log.error("检查删除约束失败", e);
            return Result.error("检查失败：" + e.getMessage());
        }
    }
    
    /**
     * 导入村庄信息
     */
    @PostMapping("/import")
    @ApiOperation("导入村庄信息")
    public Result<Map<String, Object>> importVillages(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = villageServiceIml.importVillages(file);
            return Result.success(result);
        } catch (Exception e) {
            log.error("导入村庄失败", e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }
    
    /**
     * 导出村庄信息
     */
    @GetMapping("/export")
    @ApiOperation("导出村庄信息")
    public ResponseEntity<Resource> exportVillages() {
        try {
            log.info("开始导出村庄信息");
            Resource resource = villageServiceIml.exportVillages();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"村庄信息.xlsx\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("导出村庄失败", e);
            return ResponseEntity.notFound().build();
        }
    }
}