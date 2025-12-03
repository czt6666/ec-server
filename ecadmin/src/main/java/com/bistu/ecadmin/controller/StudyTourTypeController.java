package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.StudyTourType;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.StudyTourTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/study/tour/type")
@Slf4j
@Api(tags = "研学类型管理")
public class StudyTourTypeController {

    @Autowired
    private StudyTourTypeService studyTourTypeService;

    /**
     * 创建研学类型
     */
    @PostMapping("/create")
    @ApiOperation("创建研学类型")
    public Result createStudyTourType(@RequestBody StudyTourType studyTourType) {
        log.info("创建研学类型：{}", studyTourType);
        return studyTourTypeService.createStudyTourType(studyTourType);
    }

    /**
     * 查询研学类型列表
     */
    @GetMapping("/list")
    @ApiOperation("查询研学类型列表")
    public Result<PageResult> listStudyTourTypes(
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("查询研学类型列表：typeName={}, status={}, page={}, pageSize={}", typeName, status, page, pageSize);
        return studyTourTypeService.listStudyTourTypes(typeName, status, page, pageSize);
    }

    /**
     * 更新研学类型
     */
    @PostMapping("/update")
    @ApiOperation("更新研学类型")
    public Result updateStudyTourType(@RequestBody StudyTourType studyTourType) {
        log.info("更新研学类型：{}", studyTourType);
        return studyTourTypeService.updateStudyTourType(studyTourType);
    }

    /**
     * 删除研学类型
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除研学类型")
    public Result deleteStudyTourType(@PathVariable Long id) {
        log.info("删除研学类型：id={}", id);
        return studyTourTypeService.deleteStudyTourType(id);
    }

    /**
     * 更新研学类型状态
     */
    @PostMapping("/status/update")
    @ApiOperation("更新研学类型状态")
    public Result updateStatus(@RequestBody StudyTourType studyTourType) {
        log.info("更新研学类型状态：id={}, status={}", studyTourType.getId(), studyTourType.getStatus());
        return studyTourTypeService.updateStatus(studyTourType.getId(), studyTourType.getStatus());
    }

    /**
     * 获取所有启用的研学类型（用于下拉选择）
     */
    @GetMapping("/all")
    @ApiOperation("获取所有启用的研学类型")
    public Result<List<StudyTourType>> getAllEnabledTypes() {
        log.info("获取所有启用的研学类型");
        return studyTourTypeService.getAllEnabledTypes();
    }
}