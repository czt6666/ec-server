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

    /**
     * 用户名密码注册
     * @param username 用户名
     * @param password 密码（明文）
     * @param confirmPassword 确认密码
     * @return 注册结果
     */
    Result<?> register(String username, String password, String confirmPassword);

    /**
     * 忘记密码重置
     * @param phone 手机号
     * @param code 验证码
     * @param newPassword 新密码（明文）
     * @param confirmPassword 确认新密码
     * @return 重置结果
     */
    Result<?> resetPassword(String phone, String code, String newPassword, String confirmPassword);
}

