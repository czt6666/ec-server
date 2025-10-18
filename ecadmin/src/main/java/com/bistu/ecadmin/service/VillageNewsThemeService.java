package com.bistu.ecadmin.service;

import com.alibaba.fastjson.JSONObject;

public interface VillageNewsThemeService {
    /**
     * 新增乡村新闻主题
     */
    JSONObject addVillageNewsTheme(JSONObject jsonObject);
    
    /**
     * 更新乡村新闻主题
     */
    JSONObject updateVillageNewsTheme(JSONObject jsonObject);
    
    /**
     * 删除乡村新闻主题
     */
    JSONObject deleteVillageNewsTheme(JSONObject jsonObject);
    
    /**
     * 分页查询乡村新闻主题列表
     */
    JSONObject getVillageNewsThemeList(JSONObject jsonObject);
}