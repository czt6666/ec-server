package com.bistu.system.log.dao;

import com.alibaba.fastjson.JSONObject;
import com.bistu.system.log.PO.LogRecordPO;

import java.util.List;

/**
 * @author SJJ
 * @description: 日志DAO层
 * @date 2022/8/2
 */
public interface LogDao {

    /**
     * 统计日志总数
     */
    int countLog(JSONObject jsonObject);

    /**
     * 日志列表
     */
    List<JSONObject> listLog(JSONObject jsonObject);

    //    String creatorLog(String )
    String detailLog(JSONObject jsonObject);

    /**
     * 登录日志列表
     */
    int countLoginLog(JSONObject jsonObject);
    List<JSONObject> listLoginLog(JSONObject jsonObject);

    String detailLoginLog(JSONObject jsonObject);

    /**
     * 操作日志列表
     */
    int countOperateLog(JSONObject jsonObject);
    List<JSONObject> listOperateLog(JSONObject jsonObject);

    String detailOperateLog(Long id);

    int insertOperateLog(LogRecordPO logRecordPO);
}
