package com.bistu.ecadmin.service;

import com.alibaba.fastjson.JSONObject;

public interface ActualTimeNewsService {
    /**
     * 新增实时新闻
     */
    JSONObject addActualTimeNews(JSONObject jsonObject);
    
    /**
     * 更新实时新闻
     */
    JSONObject updateActualTimeNews(JSONObject jsonObject);
    
    /**
     * 删除实时新闻
     */
    JSONObject deleteActualTimeNews(JSONObject jsonObject);
    
    /**
     * 根据ID查询实时新闻
     */
    JSONObject getActualTimeNewsById(Integer id);
    
    /**
     * 分页查询实时新闻列表
     */
    JSONObject getActualTimeNewsList(JSONObject jsonObject);
}