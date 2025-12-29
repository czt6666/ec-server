package com.bistu.ecadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.util.CommonUtil;
import com.bistu.ecadmin.service.VillageNewsThemeService;
import com.bistu.ecadmin.annotation.OperateLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/village/news/theme")
@Slf4j
public class VillageNewsThemeController {

    @Autowired
    private VillageNewsThemeService villageNewsThemeService;

    /**
     * 新增乡村新闻主题
     */
    @PostMapping(value = "/add", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @OperateLog(operation = "新增乡村新闻主题")
    public JSONObject addVillageNewsTheme(@RequestBody(required = false) JSONObject requestJson, HttpServletRequest request) {
        // 如果没有JSON请求体，则从请求参数中获取
        if (requestJson == null) {
            requestJson = CommonUtil.request2Json(request);
        }
        
        CommonUtil.hasAllRequired(requestJson, "name");
        return villageNewsThemeService.addVillageNewsTheme(requestJson);
    }

    /**
     * 更新乡村新闻主题
     */
    @PostMapping(value = "/update", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @OperateLog(operation = "更新乡村新闻主题")
    public JSONObject updateVillageNewsTheme(@RequestBody(required = false) JSONObject requestJson, HttpServletRequest request) {
        // 如果没有JSON请求体，则从请求参数中获取
        if (requestJson == null) {
            requestJson = CommonUtil.request2Json(request);
        }
        
        CommonUtil.hasAllRequired(requestJson, "id, name");
        return villageNewsThemeService.updateVillageNewsTheme(requestJson);
    }

    /**
     * 删除乡村新闻主题
     */
    @PostMapping(value = "/delete", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @OperateLog(operation = "删除乡村新闻主题")
    public JSONObject deleteVillageNewsTheme(@RequestBody(required = false) JSONObject requestJson, HttpServletRequest request) {
        // 如果没有JSON请求体，则从请求参数中获取
        if (requestJson == null) {
            requestJson = CommonUtil.request2Json(request);
        }
        
        CommonUtil.hasAllRequired(requestJson, "id");
        return villageNewsThemeService.deleteVillageNewsTheme(requestJson);
    }

    /**
     * 分页查询乡村新闻主题列表
     */
    @GetMapping("/list")
    public JSONObject getVillageNewsThemeList(HttpServletRequest request) {
        JSONObject requestJson = CommonUtil.request2Json(request);
        
        // 设置默认值
        if (!requestJson.containsKey("pageNum")) {
            requestJson.put("pageNum", 1);
        }
        if (!requestJson.containsKey("pageRow")) {
            requestJson.put("pageRow", 10);
        }
        if (!requestJson.containsKey("keyword")) {
            requestJson.put("keyword", "");
        }
        
        return villageNewsThemeService.getVillageNewsThemeList(requestJson);
    }
}