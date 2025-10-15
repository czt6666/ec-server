package com.bistu.system.log.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.system.MySqlOpenConfig;
import com.bistu.common.util.CommonUtil;
import com.bistu.system.log.PO.LogRecordPO;
import com.bistu.system.log.dao.LogDao;
import com.bistu.system.log.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SJJ
 * @description:
 * @date 2022/8/2
 */
@ConditionalOnBean({MySqlOpenConfig.class})
@Service
@Slf4j
public class LogServiceImpl implements LogService {

	private final LogDao logDao;

	@Autowired
	public LogServiceImpl(LogDao logDao) {
		this.logDao = logDao;
	}

	/**
	 * 日志列表
	 */
	public JSONObject listLog(JSONObject jsonObject) {
		CommonUtil.fillPageParam((jsonObject));
		int count = logDao.countLoginLog(jsonObject);
		List<JSONObject> list = logDao.listLoginLog(jsonObject);
		return CommonUtil.successPage(jsonObject, list, count);
	}

	public JSONObject detailLog(JSONObject jsonObject){
		String list = logDao.detailLog(jsonObject);
		return CommonUtil.successJson(list);
	}

	/**
	 * 操作日志列表
	 */
	public JSONObject listOperateLog(JSONObject jsonObject) {
		CommonUtil.fillPageParam((jsonObject));
		int count = logDao.countOperateLog(jsonObject);
		List<JSONObject> list = logDao.listOperateLog(jsonObject);
		return CommonUtil.successPage(jsonObject, list, count);
	}

	public JSONObject detailOperateLog(Long id){
		String extra = logDao.detailOperateLog(id);
		return CommonUtil.successJson(extra);
	}

	public int insertOperateLog(LogRecordPO logRecordPO){
		return logDao.insertOperateLog(logRecordPO);
	}

}
