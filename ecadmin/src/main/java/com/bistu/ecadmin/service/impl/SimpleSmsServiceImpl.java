package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 简单短信服务实现（测试用）
 * 仅在日志中输出验证码，不实际发送短信
 *
 * 当 sms.aliyun.enabled=false 时，自动使用此服务
 */
@Service
@ConditionalOnProperty(name = "sms.aliyun.enabled", havingValue = "false", matchIfMissing = true)
@Slf4j
public class SimpleSmsServiceImpl implements SmsService {

    @Override
    public boolean sendSmsCode(String phone, String code) {
        log.info("【验证码短信】手机号：{}，验证码：{}，有效期：5分钟", phone, code);
        return true;
    }
}

