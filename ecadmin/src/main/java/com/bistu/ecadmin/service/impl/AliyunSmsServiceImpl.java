package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.service.SmsService;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云短信服务实现
 *
 * 使用前需要：
 * 1. 在 pom.xml 中添加依赖：
 *    <dependency>
 *        <groupId>com.aliyun</groupId>
 *        <artifactId>aliyun-java-sdk-core</artifactId>
 *        <version>4.6.3</version>
 *    </dependency>
 *
 * 2. 在阿里云控制台申请短信服务：
 *    - 创建 AccessKey（需要 AccessKey ID 和 AccessKey Secret）
 *    - 申请短信签名（SignName）
 *    - 申请短信模板（TemplateCode），模板内容需包含 ${code} 变量
 *
 * 3. 在 application.yml 中配置：
 *    sms:
 *      aliyun:
 *        enabled: true
 *        access-key-id: your-access-key-id
 *        access-key-secret: your-access-key-secret
 *        sign-name: your-sign-name
 *        template-code: SMS_123456789
 *        region-id: cn-hangzhou
 */
@Service
@ConditionalOnProperty(name = "sms.aliyun.enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class AliyunSmsServiceImpl implements SmsService {

    @Value("${sms.aliyun.access-key-id:}")
    private String accessKeyId;

    @Value("${sms.aliyun.access-key-secret:}")
    private String accessKeySecret;

    @Value("${sms.aliyun.sign-name:}")
    private String signName;

    @Value("${sms.aliyun.template-code:}")
    private String templateCode;

    @Value("${sms.aliyun.region-id:cn-hangzhou}")
    private String regionId;

    @Override
    public boolean sendSmsCode(String phone, String code) {
        // 检查配置是否完整
        if (accessKeyId == null || accessKeyId.isEmpty() ||
            accessKeySecret == null || accessKeySecret.isEmpty() ||
            signName == null || signName.isEmpty() ||
            templateCode == null || templateCode.isEmpty()) {
            log.error("阿里云短信配置不完整，无法发送短信。请检查 application.yml 中的 sms.aliyun 配置");
            return false;
        }

        try {
            // 初始化客户端
            DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            IAcsClient client = new DefaultAcsClient(profile);

            // 构建请求
            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);
            request.setSysDomain("dysmsapi.aliyuncs.com");
            request.setSysVersion("2017-05-25");
            request.setSysAction("SendSms");
            request.putQueryParameter("RegionId", regionId);
            request.putQueryParameter("PhoneNumbers", phone);
            request.putQueryParameter("SignName", signName);
            request.putQueryParameter("TemplateCode", templateCode);

            // 构建模板参数（JSON格式）
            Map<String, String> templateParam = new HashMap<>();
            templateParam.put("code", code);
            ObjectMapper objectMapper = new ObjectMapper();
            request.putQueryParameter("TemplateParam", objectMapper.writeValueAsString(templateParam));

            // 发送请求
            CommonResponse response = client.getCommonResponse(request);
            String responseData = response.getData();

            log.info("阿里云短信发送响应：{}", responseData);

            // 解析响应判断是否成功
            if (responseData != null && responseData.contains("\"Code\":\"OK\"")) {
                log.info("短信发送成功：手机号={}, 验证码={}", phone, code);
                return true;
            } else {
                log.error("短信发送失败：手机号={}, 响应={}", phone, responseData);
                return false;
            }
        } catch (Exception e) {
            log.error("发送短信异常：手机号={}, 验证码={}", phone, code, e);
            return false;
        }
    }
}

