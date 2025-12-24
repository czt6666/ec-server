package com.bistu.system.log.service;

import com.alibaba.fastjson.JSONObject;
import com.bistu.system.log.PO.LogRecordPO;
import org.springframework.core.io.Resource;

/**
 * @author: zxh
 * @date: 2023/8/31 9:08
 * @description:
 */
public interface LogService {

	public JSONObject listLog(JSONObject jsonObject);
	public JSONObject detailLog(JSONObject jsonObject);

	public JSONObject listOperateLog(JSONObject jsonObject);
	public JSONObject detailOperateLog(Long id);

	public int insertOperateLog(LogRecordPO logRecordPO);
	
	/**
	 * 导出登录日志
	 */
	Resource exportLoginLogs(JSONObject jsonObject) throws Exception;

}
