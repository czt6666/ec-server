package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.StudyTourPlan;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.StudyTourPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 研学方案管理控制器
 */
@RestController
@RequestMapping("/study/tour/plan")
@Api(tags = "研学方案管理接口")
@Slf4j
public class StudyTourPlanController {

    @Autowired
    private StudyTourPlanService studyTourPlanService;

    /**
     * 新增研学方案
     */
    @PostMapping("/create")
    @ApiOperation(value = "新增研学方案")
    public Result create(@RequestBody StudyTourPlan studyTourPlan) {
        log.info("新增研学方案：{}", studyTourPlan);
        return studyTourPlanService.createStudyTourPlan(studyTourPlan);
    }

    /**
     * 研学方案分页查询
     */
    @GetMapping("/page")
    @ApiOperation(value = "研学方案分页查询")
    public Result<PageResult<StudyTourPlan>> page(String planName, Long baseId, Integer status,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        log.info("研学方案分页查询：planName={}, baseId={}, status={}, page={}, pageSize={}",
                planName, baseId, status, page, pageSize);
        return studyTourPlanService.listStudyTourPlans(planName, baseId, status, page, pageSize, null);
    }

    /**
     * 根据id修改研学方案信息
     */
    @PutMapping("/update")
    @ApiOperation(value = "根据id修改研学方案信息")
    public Result update(@RequestBody StudyTourPlan studyTourPlan) {
        log.info("修改研学方案：{}", studyTourPlan);
        return studyTourPlanService.updateStudyTourPlan(studyTourPlan);
    }

    /**
     * 根据id删除研学方案
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "根据id删除研学方案")
    public Result delete(@PathVariable Long id) {
        log.info("删除研学方案：id={}", id);
        return studyTourPlanService.deleteStudyTourPlan(id);
    }

    /**
     * 查询所有启用的研学方案（用于下拉选择）
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有启用的研学方案")
    public Result<List<StudyTourPlan>> list() {
        log.info("查询所有启用的研学方案");
        return studyTourPlanService.listAllEnabled();
    }
}