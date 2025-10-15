package com.bistu.system.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.common.config.system.MySqlOpenConfig;
import com.bistu.common.util.CommonUtil;
import com.bistu.common.util.EncryptionUtil;
import com.bistu.system.login.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

/**
 * @author: SJJ
 * @description: 登录相关Controller
 * @date: 2022/6/16
 */
@ConditionalOnBean({MySqlOpenConfig.class})
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

	@Autowired
	private LoginService loginService;

	/**
	 * 登录
	 */
//    @VLicense
	@PostMapping("/auth")
	public JSONObject authLogin(@RequestBody JSONObject requestJson) throws NoSuchAlgorithmException, EncryptionUtil.EncryptException {
		CommonUtil.hasAllRequired(requestJson, "username,password");
		return loginService.authLogin(requestJson);
	}

	/**
	 * 查询当前登录用户的信息
	 */
	@PostMapping("/getInfo")
	public JSONObject getInfo() {
		return loginService.getInfo();
	}

	/**
	 * 登出
	 */
	@PostMapping("/logout")
	public JSONObject logout() {
		return loginService.logout();
	}

	/**
	 * 刷新
	 */
	@RequiresPermissions("refresh")
	@PostMapping("/refresh")
	public JSONObject refresh() {
		return CommonUtil.successJson();
	}
}
