package com.bistu.ecadmin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.util.CommonUtil;
import com.bistu.common.util.constants.ErrorEnum;
import com.bistu.ecadmin.dao.NewsThemeDao;
import com.bistu.ecadmin.dao.VillageNewsDao;
import com.bistu.ecadmin.service.VillageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VillageServiceImpl implements VillageService {
    
    @Autowired
    private VillageNewsDao villageNewsDao;
    
    @Autowired
    private NewsThemeDao newsThemeDao; // 添加这行
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addVillageNews(JSONObject requestJson) {
        try {
            // 验证必填字段
            CommonUtil.hasAllRequired(requestJson, "title, author, villageName, themeName, content");
            
            // 1. 从 JSON 取出 themeName 字符串
            String themeNameStr = requestJson.getString("themeName");
            List<String> themeNames = Arrays.asList(themeNameStr.split(","));
            
            // 3. 拿到对应的 theme_id 列表
            List<Integer> themeIds = newsThemeDao.selectIdsByNames(themeNames);
            
            // 设置 publish_status 默认值为1（草稿状态）
            if (!requestJson.containsKey("publish_status")) {
                requestJson.put("publish_status", 1);
            }
            
            // 4. 先插新闻表，拿到自增主键
            int result = villageNewsDao.addVillageNews(requestJson); // news.getId() 已回填
            
            // 获取插入的新闻ID
            Integer newsId = requestJson.getInteger("id");
            
            // 5. 批量插中间表
            if (!themeIds.isEmpty() && newsId != null) {
                newsThemeDao.insertBatch(newsId, themeIds);
            }
            
            return CommonUtil.successJson();
        } catch (Exception e) {
            log.error("新增乡村新闻失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateVillageNews(JSONObject jsonObject) {
        try {
            // 验证必填字段
            CommonUtil.hasAllRequired(jsonObject, "id, title, author, villageName, themeName, content");
            
            // 1. 从 JSON 取出 themeName 字符串
            String themeNameStr = jsonObject.getString("themeName");
            List<String> themeNames = Arrays.asList(themeNameStr.split(","));
            
            // 2. 拿到对应的 theme_id 列表
            List<Integer> themeIds = newsThemeDao.selectIdsByNames(themeNames);
            
            // 3. 先更新新闻表
            int result = villageNewsDao.updateVillageNews(jsonObject);
            
            if (result > 0) {
                // 4. 获取新闻ID
                Integer newsId = jsonObject.getInteger("id");
                
                // 5. 删除原有的新闻主题关联关系
                if (newsId != null) {
                    newsThemeDao.deleteByNewsId(newsId);
                    
                    // 6. 批量插入新的新闻主题关联关系
                    if (!themeIds.isEmpty()) {
                        newsThemeDao.insertBatch(newsId, themeIds);
                    }
                }
                
                return CommonUtil.successJson();
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_400);
            }
        } catch (Exception e) {
            log.error("更新乡村新闻失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject deleteVillageNews(JSONObject jsonObject) {
        try {
            // 调用DAO删除乡村新闻（逻辑删除）
            int result = villageNewsDao.deleteVillageNews(jsonObject);
            if (result > 0) {
                return CommonUtil.successJson();
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_400);
            }
        } catch (Exception e) {
            log.error("删除乡村新闻失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    public JSONObject getVillageNewsList(JSONObject jsonObject) {
        try {
            // 确保pageNum和pageRow是整数类型
            int pageNum = jsonObject.getIntValue("pageNum");
            int pageRow = jsonObject.getIntValue("pageRow");
            String keyword = jsonObject.getString("keyword");
            // 获取发布状态参数
            Integer publishStatus = jsonObject.getInteger("publish_status");
            
            // 计算偏移量
            int offSet = (pageNum - 1) * pageRow;
            
            // 创建新的JSONObject确保参数类型正确
            JSONObject params = new JSONObject();
            params.put("offSet", offSet);
            params.put("pageRow", pageRow);
            params.put("keyword", keyword);
            // 传递发布状态参数
            if (publishStatus != null) {
                params.put("publish_status", publishStatus);
            }
            
            // 查询总记录数
            int totalCount = villageNewsDao.countVillageNews(params);
            
            // 查询列表数据
            List<JSONObject> list = villageNewsDao.listVillageNews(params);
            
            // 如果有数据，查询并设置主题名称
            if (list != null && !list.isEmpty()) {
                // 提取所有新闻ID
                List<Integer> newsIds = new ArrayList<>();
                for (JSONObject news : list) {
                    newsIds.add(news.getInteger("id"));
                }
                
                // 根据新闻ID查询主题名称
                List<JSONObject> themeNameList = newsThemeDao.selectThemeNamesByNewsIds(newsIds);
                
                // 构建新闻ID到主题名称列表的映射
                Map<Integer, List<String>> newsThemesMap = new HashMap<>();
                for (JSONObject theme : themeNameList) {
                    Integer newsId = theme.getInteger("newsId");
                    String themeName = theme.getString("themeName");
                    
                    if (!newsThemesMap.containsKey(newsId)) {
                        newsThemesMap.put(newsId, new ArrayList<>());
                    }
                    newsThemesMap.get(newsId).add(themeName);
                }
                
                // 为每个新闻设置拼接后的主题名称
                for (JSONObject news : list) {
                    Integer newsId = news.getInteger("id");
                    if (newsThemesMap.containsKey(newsId)) {
                        String themeNames = String.join(",", newsThemesMap.get(newsId));
                        news.put("themeName", themeNames);
                    } else {
                        news.put("themeName", "");
                    }
                }
            }
            
            // 构建返回结果
            JSONObject result = new JSONObject();
            result.put("list", list);
            result.put("totalCount", totalCount);
            result.put("pageNum", pageNum);
            result.put("pageRow", pageRow);
            
            return CommonUtil.successJson(result);
        } catch (Exception e) {
            log.error("查询乡村新闻列表失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }
}