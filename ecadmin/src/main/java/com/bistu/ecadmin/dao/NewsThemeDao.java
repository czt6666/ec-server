package com.bistu.ecadmin.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author: NewsTheme
 * @description: 新闻主题关联相关dao
 * @date: 2025/10
 */
public interface NewsThemeDao {
    /**
     * 批量插入新闻主题关联
     */
    int insertBatch(@Param("newsId") Integer newsId, @Param("themeIds") List<Integer> themeIds);
    
    /**
     * 根据新闻ID删除关联关系
     */
    int deleteByNewsId(Integer newsId);
    
    /**
     * 根据主题名称列表查询主题ID列表
     */
    List<Integer> selectIdsByNames(List<String> themeNames);
    
    /**
     * 根据新闻ID列表查询主题名称
     */
    List<JSONObject> selectThemeNamesByNewsIds(@Param("newsIds") List<Integer> newsIds);
}