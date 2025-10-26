package com.bistu.ecadmin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.util.CommonUtil;
import com.bistu.common.util.constants.ErrorEnum;
import com.bistu.ecadmin.dao.VillageNewsDao;
import com.bistu.ecadmin.service.VillageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class VillageServiceImpl implements VillageService {

    @Autowired
    private VillageNewsDao villageNewsDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addVillageNews(JSONObject jsonObject) {
        try {
            // 直接调用DAO，让数据库自动生成ID
            int result = villageNewsDao.addVillageNews(jsonObject);
            if (result > 0) {
                return CommonUtil.successJson();
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_400);
            }
        } catch (Exception e) {
            log.error("处理乡村新闻失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateVillageNews(JSONObject jsonObject) {
        try {
            // 调用DAO更新乡村新闻
            int result = villageNewsDao.updateVillageNews(jsonObject);
            if (result > 0) {
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