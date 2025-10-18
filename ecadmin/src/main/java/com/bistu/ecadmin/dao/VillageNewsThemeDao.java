package com.bistu.ecadmin.dao;

import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 * @author: VillageNewsTheme
 * @description: 乡村新闻主题相关dao
 * @date: 2024/12
 */
public interface VillageNewsThemeDao {
    /**
     * 新增乡村新闻主题
     */
    int addVillageNewsTheme(JSONObject jsonObject);
    
    /**
     * 更新乡村新闻主题
     */
    int updateVillageNewsTheme(JSONObject jsonObject);
    
    /**
     * 删除乡村新闻主题（逻辑删除）
     */
    int deleteVillageNewsTheme(JSONObject jsonObject);
    
    /**
     * 查询乡村新闻主题数量
     */
    int countVillageNewsTheme(JSONObject jsonObject);
    
    /**
     * 分页查询乡村新闻主题列表
     */
    List<JSONObject> listVillageNewsTheme(JSONObject jsonObject);
}