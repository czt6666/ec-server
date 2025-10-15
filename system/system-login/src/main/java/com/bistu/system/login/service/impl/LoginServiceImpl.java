package com.bistu.system.login.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.exception.CommonJsonException;
import com.bistu.common.config.system.MySqlOpenConfig;
import com.bistu.common.config.system.RSACipherConfig;
import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.CommonUtil;
import com.bistu.common.util.EncryptionUtil;
import com.bistu.common.util.TokenUtil;
import com.bistu.common.util.constants.ErrorEnum;
import com.bistu.system.login.service.LoginService;
import com.bistu.system.login.dao.LoginDao;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.security.NoSuchAlgorithmException;

/**
 * @author: SJJ
 * @description: 登录service实现类
 * @date: 2022/6/16
 */
@ConditionalOnBean({MySqlOpenConfig.class})
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
	private final Logger loginLog = LoggerFactory.getLogger("com.bistu.system");

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private TokenUtil tokenService;


	/**
	 * 登录表单提交
	 */
	public JSONObject authLogin(JSONObject jsonObject) throws NoSuchAlgorithmException, EncryptionUtil.EncryptException {
		long stime = System.currentTimeMillis();
		String username = jsonObject.getString("username");
		String password = jsonObject.getString("password");
		RSACipherConfig rsaCipherConfig = new RSACipherConfig();
		rsaCipherConfig.setPrivateKey(rsaCipherConfig.getDEFAULT_PRIVATEKEY());
		// 20250925取消加解密
		 String passwordde = EncryptionUtil.decrypt(password, rsaCipherConfig.getPrivateKey());
		 String pwdMd = DigestUtils.md5DigestAsHex(passwordde.getBytes());

		JSONObject info = new JSONObject();
		JSONObject user = loginDao.checkUser(username, pwdMd);
//		JSONObject user = loginDao.checkUser(username, password);
		if (user == null) {
			// 记录登录失败日志
			String nickname = "无";
			String tag = "read";
			long etime = System.currentTimeMillis();
			String time = String.valueOf(etime - stime);
			String operation = "登录失败";
			loginLog.error("{可读}{执行时长}{操作人}{操作名}", tag, time, nickname, operation);
//			log.error("账号/密码错误");
			throw new CommonJsonException(ErrorEnum.E_10010);
		}

		/** 2023/12/13-ts-add: 添加获取userInfo，并将userinfo作为参数传给tokenService用于生成token(解耦)*/
		SessionUserInfo userInfo = loginDao.getUserInfo(username);
		if (userInfo.getRoleIds().contains(1)) {
			// 管理员,查出全部按钮和权限码
			userInfo.setMenuList(loginDao.getAllMenu());
			userInfo.setPermissionList(loginDao.getAllPermissionCode());
		}
		String token = tokenService.generateToken(username, userInfo);
		info.put("token", token);
		// 记录登录成功日志
		String nickname = userInfo.getNickname();
		String tag = "read";
		long etime = System.currentTimeMillis();
		String time = String.valueOf(etime - stime);
		String operation = "登录成功";
		loginLog.error("登录日志：可读={}，执行时长={}，操作人={}，操作名={}", tag, time, nickname, operation);
		return CommonUtil.successJson(info);
	}

	/**
	 * 查询当前登录用户的权限等信息
	 */
	public JSONObject getInfo() {
		// 从session获取用户信息
		SessionUserInfo userInfo = tokenService.getUserInfo();
		log.info(userInfo.toString());
		return CommonUtil.successJson(userInfo);
	}

	/**
	 * 退出登录
	 */
	public JSONObject logout() {
		long stime = System.currentTimeMillis();
		/**
		 * 2023.01.16,hyj
		 * 用户登出时删除该用户的redis缓存
		 */
		SessionUserInfo userInfo = tokenService.getUserInfo();
		tokenService.invalidateToken();
		// 记录注销登录日志
		String nickname = userInfo.getNickname();
		String tag = "read";
		long etime = System.currentTimeMillis();
		String time = String.valueOf(etime - stime);
		String operation = "注销登录";
		loginLog.error("登录日志：可读={}，执行时长={}，操作人={}，操作名={}", tag, time, nickname, operation);
		return CommonUtil.successJson();
	}
}
