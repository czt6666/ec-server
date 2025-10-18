package com.bistu.ecadmin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.util.CommonUtil;
import com.bistu.common.util.constants.ErrorEnum;
import com.bistu.ecadmin.dao.VillageNewsThemeDao;
import com.bistu.ecadmin.service.VillageNewsThemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class VillageNewsThemeServiceImpl implements VillageNewsThemeService {

    @Autowired
    private VillageNewsThemeDao villageNewsThemeDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addVillageNewsTheme(JSONObject jsonObject) {
        try {
            // 直接调用DAO，让数据库自动生成ID
            int result = villageNewsThemeDao.addVillageNewsTheme(jsonObject);
            if (result > 0) {
                return CommonUtil.successJson();
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_400);
            }
        } catch (Exception e) {
            log.error("处理乡村新闻主题失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateVillageNewsTheme(JSONObject jsonObject) {
        try {
            // 调用DAO更新乡村新闻主题
            int result = villageNewsThemeDao.updateVillageNewsTheme(jsonObject);
            if (result > 0) {
                return CommonUtil.successJson();
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_400);
            }
        } catch (Exception e) {
            log.error("更新乡村新闻主题失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject deleteVillageNewsTheme(JSONObject jsonObject) {
        try {
            // 调用DAO删除乡村新闻主题（逻辑删除）
            int result = villageNewsThemeDao.deleteVillageNewsTheme(jsonObject);
            if (result > 0) {
                return CommonUtil.successJson();
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_400);
            }
        } catch (Exception e) {
            log.error("删除乡村新闻主题失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    public JSONObject getVillageNewsThemeList(JSONObject jsonObject) {
        try {
            // 确保pageNum和pageRow是整数类型
            int pageNum = jsonObject.getIntValue("pageNum");
            int pageRow = jsonObject.getIntValue("pageRow");
            String keyword = jsonObject.getString("keyword");
            
            // 计算偏移量
            int offSet = (pageNum - 1) * pageRow;
            
            // 创建新的JSONObject确保参数类型正确
            JSONObject params = new JSONObject();
            params.put("offSet", offSet);
            params.put("pageRow", pageRow);
            params.put("keyword", keyword);
            
            // 查询总记录数
            int totalCount = villageNewsThemeDao.countVillageNewsTheme(params);
            
            // 查询列表数据
            List<JSONObject> list = villageNewsThemeDao.listVillageNewsTheme(params);
            
            // 构建返回结果
            JSONObject result = new JSONObject();
            result.put("list", list);
            result.put("totalCount", totalCount);
            result.put("pageNum", pageNum);
            result.put("pageRow", pageRow);
            
            return CommonUtil.successJson(result);
        } catch (Exception e) {
            log.error("查询乡村新闻主题列表失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }
}