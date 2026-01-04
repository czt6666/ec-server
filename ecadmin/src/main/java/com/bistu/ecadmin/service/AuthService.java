package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.Result;

import java.util.Map;

public interface AuthService {

    /**
     * 发送手机验证码
     * @param phone 手机号
     * @return 结果
     */
    Result<?> sendSmsCode(String phone);

    /**
     * 手机号验证码登录
     * @param phone 手机号
     * @param code 验证码
     * @return 登录结果，包含token和用户信息
     */
    Result<Map<String, Object>> loginByPhone(String phone, String code);

    /**
     * 用户名密码登录
     * @param username 用户名
     * @param password 密码（明文）
     * @return 登录结果，包含token和用户信息
     */
    Result<Map<String, Object>> loginByUsername(String username, String password);
}

