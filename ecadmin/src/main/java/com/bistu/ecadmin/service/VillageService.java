package com.bistu.ecadmin.service;

import com.alibaba.fastjson.JSONObject;

public interface VillageService {
    /**
     * 新增乡村新闻
     */
    JSONObject addVillageNews(JSONObject jsonObject);
    
    /**
     * 更新乡村新闻
     */
    JSONObject updateVillageNews(JSONObject jsonObject);
    
    /**
     * 删除乡村新闻
     */
    JSONObject deleteVillageNews(JSONObject jsonObject);
    
    /**
     * 分页查询乡村新闻列表
     */
    JSONObject getVillageNewsList(JSONObject jsonObject);
}