package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.StudyTourBase;
import com.bistu.ecadmin.pojo.StudyTourType;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.StudyTourBaseService;
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
    @PostMapping("/create")
    @ApiOperation(value = "新增研学基地")
    public Result create(@RequestBody StudyTourBase studyTourBase) {
        log.info("新增研学基地：{}", studyTourBase);
        return studyTourBaseService.createStudyTourBase(studyTourBase);
    }

    /**
     * 研学基地分页查询
     */
    @GetMapping("/page")
    @ApiOperation(value = "研学基地分页查询")
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
    @PutMapping("/update")
    @ApiOperation(value = "根据id修改研学基地信息")
    public Result update(@RequestBody StudyTourBase studyTourBase) {
        log.info("修改研学基地：{}", studyTourBase);
        return studyTourBaseService.updateStudyTourBase(studyTourBase);
    }

    /**
     * 根据id删除研学基地
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "根据id删除研学基地")
    public Result delete(@PathVariable Long id) {
        log.info("删除研学基地：id={}", id);
        return studyTourBaseService.deleteStudyTourBase(id);
    }

    /**
     * 保存基地与研学类型的关联关系
     */
    @PostMapping("/saveBaseTypes")
    @ApiOperation(value = "保存基地与研学类型的关联关系")
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
}