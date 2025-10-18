package com.bistu.ecadmin.dao;

import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 * @author: VillageNews
 * @description: 乡村新闻相关dao
 * @date: 2024/12
 */
public interface VillageNewsDao {
    /**
     * 新增乡村新闻
     */
    int addVillageNews(JSONObject jsonObject);
    
    /**
     * 更新乡村新闻
     */
    int updateVillageNews(JSONObject jsonObject);
    
    /**
     * 删除乡村新闻（逻辑删除）
     */
    int deleteVillageNews(JSONObject jsonObject);
    
    /**
     * 查询乡村新闻数量
     */
    int countVillageNews(JSONObject jsonObject);
    
    /**
     * 分页查询乡村新闻列表
     */
    List<JSONObject> listVillageNews(JSONObject jsonObject);
}