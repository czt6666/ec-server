package com.bistu.ecadmin.service;

/**
 * 短信服务接口
 * 用于发送验证码短信
 */
public interface SmsService {

    /**
     * 发送验证码短信
     * @param phone 手机号
     * @param code 验证码
     * @return 是否发送成功
     */
    boolean sendSmsCode(String phone, String code);
}

