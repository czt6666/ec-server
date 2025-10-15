package com.bistu.system.login.service;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.util.EncryptionUtil;

import java.security.NoSuchAlgorithmException;

/**
 * @author: zxh
 * @date: 2023/8/31 9:08
 * @description:
 */
public interface LoginService {

	public JSONObject authLogin(JSONObject jsonObject) throws NoSuchAlgorithmException, EncryptionUtil.EncryptException;

	public JSONObject getInfo();

	public JSONObject logout();
}
