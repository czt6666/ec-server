package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.StudyTourActivity;
import com.bistu.ecadmin.service.StudyTourActivityService;
import com.bistu.ecadmin.pojo.Result;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/study/activity")
public class StudyTourActivityController {
    
    @Autowired
    private StudyTourActivityService studyTourActivityService;
    
    /**
     * 分页查询研学活动
     */
    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false) String activityName,
                       @RequestParam(required = false) Long planId,
                       @RequestParam(required = false) Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        List<StudyTourActivity> list = studyTourActivityService.list(activityName, planId, status, null);
        PageInfo<StudyTourActivity> pageInfo = new PageInfo<>(list);
        return Result.success(pageInfo);
    }
    
    /**
     * 新增研学活动
     */
    @PostMapping
    public Result save(@RequestBody StudyTourActivity studyTourActivity) {
        studyTourActivityService.save(studyTourActivity);
        return Result.success();
    }
    
    /**
     * 根据id查询研学活动
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        StudyTourActivity studyTourActivity = studyTourActivityService.getById(id, null);
        return Result.success(studyTourActivity);
    }
    
    /**
     * 更新研学活动
     */
    @PutMapping
    public Result update(@RequestBody StudyTourActivity studyTourActivity) {
        studyTourActivityService.update(studyTourActivity);
        return Result.success();
    }
    
    /**
     * 删除研学活动
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        studyTourActivityService.deleteById(id);
        return Result.success();
    }
    
    /**
     * 获取所有启用的研学活动
     */
    @GetMapping("/list")
    public Result listAllEnabled() {
        List<StudyTourActivity> list = studyTourActivityService.listAllEnabled(null);
        return Result.success(list);
    }
}