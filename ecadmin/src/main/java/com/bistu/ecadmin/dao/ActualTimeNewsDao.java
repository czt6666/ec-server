package com.bistu.ecadmin.dao;

import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 * @author: ActualTimeNews
 * @description: 实时新闻相关dao
 * @date: 2026/01
 */
public interface ActualTimeNewsDao {
    /**
     * 新增实时新闻
     */
    int addActualTimeNews(JSONObject jsonObject);
    
    /**
     * 更新实时新闻
     */
    int updateActualTimeNews(JSONObject jsonObject);
    
    /**
     * 删除实时新闻（逻辑删除）
     */
    int deleteActualTimeNews(JSONObject jsonObject);
    
    /**
     * 根据ID查询实时新闻
     */
    JSONObject getActualTimeNewsById(Integer id);
    
    /**
     * 查询实时新闻数量
     */
    int countActualTimeNews(JSONObject jsonObject);
    
    /**
     * 分页查询实时新闻列表
     */
    List<JSONObject> listActualTimeNews(JSONObject jsonObject);
}