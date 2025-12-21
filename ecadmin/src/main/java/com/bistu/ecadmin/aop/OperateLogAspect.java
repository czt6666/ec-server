package com.bistu.ecadmin.aop;

import com.bistu.ecadmin.annotation.OperateLog;
import com.bistu.ecadmin.service.OperateLogService;
import com.bistu.ecadmin.service.UserHeaderService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class OperateLogAspect {
    
    @Autowired
    private OperateLogService operateLogService;
    
    @Autowired
    private UserHeaderService userHeaderService;
    
    @Around("@annotation(com.bistu.ecadmin.annotation.OperateLog)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前HTTP请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }
        
        // 创建操作日志对象
        com.bistu.ecadmin.pojo.OperateLog operateLog = new com.bistu.ecadmin.pojo.OperateLog();
        
        // 从请求头X-User-Id获取用户信息
        try {
            // 从请求头X-User-Id获取用户ID
            String userIdStr = null;
            if (request != null) {
                userIdStr = request.getHeader("X-User-Id");
            }
            
            if (userIdStr != null && !userIdStr.isEmpty()) {
                try {
                    Long userId = Long.parseLong(userIdStr);
                    operateLog.setUserId(userId);
                    // 根据用户ID查询用户名
                    String username = userHeaderService.getUsernameByUserId(userId);
                    operateLog.setUsername(username != null ? username : "Unknown");
                    System.out.println("从请求头X-User-Id获取到的用户信息: userId=" + userId + ", username=" + username);
                } catch (NumberFormatException e) {
                    System.out.println("X-User-Id格式不正确: " + userIdStr);
                    // 使用默认值
                    operateLog.setUserId(1L);
                    operateLog.setUsername("admin");
                }
            } else {
                // 如果没有X-User-Id，使用默认值
                System.out.println("请求头中未找到X-User-Id，使用默认值");
                operateLog.setUserId(1L);
                operateLog.setUsername("admin");
            }
        } catch (Exception e) {
            // 如果无法获取用户信息，则使用默认值
            System.out.println("无法获取用户信息，使用默认值: " + e.getMessage());
            operateLog.setUserId(1L); // 默认用户ID
            operateLog.setUsername("admin"); // 默认用户名
        }
        
        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.bistu.ecadmin.annotation.OperateLog annotation = method.getAnnotation(com.bistu.ecadmin.annotation.OperateLog.class);
        
        // 设置操作描述和类型
        operateLog.setOperation(annotation.operation());
        operateLog.setMethod(method.getDeclaringClass().getName() + "." + method.getName());
        
        // 记录请求URL而不是参数内容
        if (request != null) {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(request.getRequestURL());
            if (request.getQueryString() != null) {
                urlBuilder.append("?").append(request.getQueryString());
            }
            operateLog.setRequestParams(urlBuilder.toString());
        } else {
            operateLog.setRequestParams("无法获取请求URL");
        }
        
        // 记录IP地址
        if (request != null) {
            operateLog.setIpAddress(getIpAddress(request));
        }
        
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        operateLog.setCreateTime(new Date(startTime));
        
        try {
            // 执行目标方法
            Object result = joinPoint.proceed();
            
            // 不再序列化响应结果，直接记录简单信息
            operateLog.setResponseResult("方法执行完成");
            
            // 记录执行状态和耗时
            operateLog.setSuccess(1); // 成功
            operateLog.setCostTime(System.currentTimeMillis() - startTime);
            
            // 保存操作日志
            operateLogService.saveOperateLog(operateLog);
            
            return result;
        } catch (Exception e) {
            // 记录异常信息
            operateLog.setSuccess(0); // 失败
            operateLog.setErrorMessage(e.getMessage());
            operateLog.setCostTime(System.currentTimeMillis() - startTime);
            
            // 保存操作日志
            operateLogService.saveOperateLog(operateLog);
            
            // 抛出异常
            throw e;
        }
    }
    
    /**
     * 获取请求IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}