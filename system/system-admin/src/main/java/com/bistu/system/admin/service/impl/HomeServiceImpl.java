package com.bistu.system.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.system.MySqlOpenConfig;
import com.bistu.common.util.CommonUtil;
import com.bistu.common.util.TokenUtil;
import com.bistu.system.admin.service.HomeService;
import com.bistu.system.admin.dao.LoginDao;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

/**
 * @author: SJJ
 * @description: 登录service实现类
 * @date: 2022/6/16
 */
@ConditionalOnBean({MySqlOpenConfig.class})
@Service
@Slf4j
public class HomeServiceImpl implements HomeService {
	private final Logger loginLog = LoggerFactory.getLogger("com.bistu.system");

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private TokenUtil tokenService;


	/**
	 * 查询当前登录用户的权限等信息
	 */
	public JSONObject getHome() {
		return CommonUtil.successJson("hello!");
	}

}
