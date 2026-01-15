package com.bistu.ecadmin.controller;

import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.ecadmin.pojo.StudyTourBase;
import com.bistu.ecadmin.pojo.StudyTourType;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.StudyTourBaseService;
import com.bistu.ecadmin.annotation.OperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 研学基地管理控制器
 */
@RestController
@RequestMapping("/study/tour/base")
@Api(tags = "研学基地管理接口")
@Slf4j
public class StudyTourBaseController {

    @Autowired
    private StudyTourBaseService studyTourBaseService;

    /**
     * 新增研学基地
     */
    @RequiresPermissions("studyBase:add")
    @PostMapping("/create")
    @ApiOperation(value = "新增研学基地")
    @OperateLog(operation = "新增研学基地")
    public Result create(@RequestBody StudyTourBase studyTourBase) {
        log.info("新增研学基地：{}", studyTourBase);
        return studyTourBaseService.createStudyTourBase(studyTourBase);
    }

    /**
     * 研学基地分页查询（小程序可访问）
     */
    @GetMapping("/page")
    @ApiOperation(value = "研学基地分页查询（小程序可访问）")
    public Result<PageResult<StudyTourBase>> page(String baseName, String operationUnit, Integer businessStatus,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        log.info("研学基地分页查询：baseName={}, operationUnit={}, businessStatus={}, page={}, pageSize={}",
                baseName, operationUnit, businessStatus, page, pageSize);
        return studyTourBaseService.listStudyTourBases(baseName, operationUnit, businessStatus, page, pageSize);
    }

    /**
     * 根据id修改研学基地信息
     */
    @RequiresPermissions("studyBase:update")
    @PutMapping("/update")
    @ApiOperation(value = "根据id修改研学基地信息")
    @OperateLog(operation = "更新研学基地")
    public Result update(@RequestBody StudyTourBase studyTourBase) {
        log.info("修改研学基地：{}", studyTourBase);
        return studyTourBaseService.updateStudyTourBase(studyTourBase);
    }

    /**
     * 根据id删除研学基地
     */
    @RequiresPermissions("studyBase:delete")
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "根据id删除研学基地")
    @OperateLog(operation = "删除研学基地")
    public Result delete(@PathVariable Long id) {
        log.info("删除研学基地：id={}", id);
        return studyTourBaseService.deleteStudyTourBase(id);
    }

    /**
     * 保存基地与研学类型的关联关系
     */
    @RequiresPermissions("studyBase:update")
    @PostMapping("/saveBaseTypes")
    @ApiOperation(value = "保存基地与研学类型的关联关系")
    @OperateLog(operation = "保存基地与研学类型的关联关系")
    public Result saveBaseTypes(@RequestParam Long baseId, @RequestBody List<Long> typeIds) {
        log.info("保存基地与研学类型的关联关系：baseId={}, typeIds={}", baseId, typeIds);
        return studyTourBaseService.saveBaseTypes(baseId, typeIds);
    }

    /**
     * 获取基地关联的研学类型
     */
    @GetMapping("/getAssociatedTypes/{baseId}")
    @ApiOperation(value = "获取基地关联的研学类型")
    public Result<List<StudyTourType>> getAssociatedTypes(@PathVariable Long baseId) {
        log.info("获取基地关联的研学类型：baseId={}", baseId);
        return studyTourBaseService.getAssociatedTypes(baseId);
    }

    /**
     * 查询研学基地列表（不分页，用于下拉选择）
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询研学基地列表")
    public Result<List<StudyTourBase>> list() {
        log.info("查询研学基地列表");
        return studyTourBaseService.listStudyTourBases();
    }

    /**
     * 上架研学基地（仅管理员）
     */
    @RequiresPermissions("studyBase:publish")
    @PostMapping("/{id}/publish")
    @ApiOperation(value = "上架研学基地（仅管理员，将营业状态改为1-营业中）")
    @OperateLog(operation = "上架研学基地")
    public Result<?> publish(@PathVariable Long id) {
        log.info("上架研学基地：id={}", id);
        try {
            boolean success = studyTourBaseService.publish(id);
            return success ? Result.success("上架成功") : Result.error("上架失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 下架研学基地（仅管理员，将营业状态改为2-待审核/下架）
     */
    @RequiresPermissions("studyBase:unpublish")
    @PostMapping("/{id}/unpublish")
    @ApiOperation(value = "下架研学基地（仅管理员，将营业状态改为2-待审核/下架）")
    @OperateLog(operation = "下架研学基地")
    public Result<?> unpublish(@PathVariable Long id) {
        log.info("下架研学基地：id={}", id);
        try {
            boolean success = studyTourBaseService.unpublish(id);
            return success ? Result.success("下架成功") : Result.error("下架失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}