package com.bistu.common.util;


import com.bistu.common.dto.session.SessionUserInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.bistu.common.config.exception.CommonJsonException;
import com.bistu.common.util.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

//@ConditionalOnBean({MySqlOpenConfigSystem.class})
@Service
@Slf4j
public class TokenUtil {

	@Autowired
	Cache<String, SessionUserInfo> cacheMap;

//	@Autowired
//	LoginDao loginDao;


	/**
	 * 用户登录验证通过后(sso/帐密),生成token,记录用户已登录的状态
	 */
	/**
	 * 2023/12/13-ts-update: 添加SessionUserInfo userInfo参数
	 */
	public String generateToken(String username, SessionUserInfo userInfo) {
		MDC.put("username", username);
		String token = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
		// 设置用户信息缓存
		setCache(token, username, userInfo);
		return token;
	}

	public SessionUserInfo getUserInfo() {
		String token = MDC.get("token");
		return getUserInfoFromCache(token);
	}

	/**
	 * 根据token查询用户信息
	 * 如果token无效,会抛未登录的异常
	 */
	public SessionUserInfo getUserInfoFromCache(String token) {
		/*log.debug(token);
		if (StringTools.isNullOrEmpty(token)) {
			log.debug("token是否存在{}", !StringTools.isNullOrEmpty(token));
			*//***
		 * 20230113，sjj防止token为空跳回登录
		 * RequestFilter.java中doFilterInternal拦截器放行打开文件及放缩
		 *//*
			// 20230113,sjj,如果token为空放行
			log.debug("当前无用户登录,假设admin");
			SessionUserInfo info = new SessionUserInfo();
			info.setUserId(10003);
			info.setUsername("admin");
			info.setNickname("超级用户");
			List<Integer> Ids = new ArrayList<>();
			Ids.add(1);
			info.setRoleIds(Ids);
			Set<String> permission = new HashSet<String>();
			permission.add("viewer:view");
			permission.add("viewer:download");
			info.setPermissionList(permission);
			return info;
			// throw new CommonJsonException(ErrorEnum.E_20011);
		}*/

		log.debug("根据token从缓存中查询用户信息,{}", token);
		if (token == null) {
			log.info("token为null");
			throw new CommonJsonException(ErrorEnum.E_20011);
		}
		SessionUserInfo info = cacheMap.getIfPresent(token);
		if (info == null) {
			log.info("没拿到缓存 token={}", token);
			throw new CommonJsonException(ErrorEnum.E_20011);
		}
		return info;
	}

	/**
	 * 2023/12/13-ts-update: 添加SessionUserInfo userInfo参数
	 */
	public void setCache(String token, String username, SessionUserInfo userInfo) {
//		SessionUserInfo info = getUserInfoByUsername(username);
		log.info("设置用户信息缓存:token={} , username={}, info={}", token, username, userInfo);
		cacheMap.put(token, userInfo);
	}

	/**
	 * 退出登录时,将token置为无效
	 */
	public void invalidateToken() {
		String token = MDC.get("token");
		if (!StringTools.isNullOrEmpty(token)) {
			cacheMap.invalidate(token);
		}
		log.debug("退出登录,清除缓存:token={}", token);
	}
/** 2023/12/13-ts-update: 注释通过logindao获取userinfo的代码，将逻辑放在loginservice中，解耦*/
//	public SessionUserInfo getUserInfoByUsername(String username) {
//		SessionUserInfo userInfo = loginDao.getUserInfo(username);
//		if (userInfo.getRoleIds().contains(1)) {
//			// 管理员,查出全部按钮和权限码
//			userInfo.setMenuList(loginDao.getAllMenu());
//			userInfo.setPermissionList(loginDao.getAllPermissionCode());
//		}
//		return userInfo;
//	}
}
