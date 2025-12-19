package com.bistu.ecadmin.controller;

import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.ecadmin.annotation.OperateLog;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.UserHeaderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户上下文测试控制器
 * 用于验证新的用户信息传递机制
 */
@RestController
@RequestMapping("/admin/ecadmin/user-context-test")
@Api(tags = "用户上下文测试")
@Slf4j
public class UserContextTestController {

    @Autowired
    private UserHeaderService userHeaderService;

    /**
     * 测试从请求头X-User-Id获取用户信息
     */
    @GetMapping("/current-user")
    @ApiOperation("获取当前用户信息")
    @OperateLog(operation = "获取当前用户信息")
    public Result<SessionUserInfo> getCurrentUser(HttpServletRequest request) {
        try {
            // 从请求头中获取X-User-Id
            String userIdStr = request.getHeader("X-User-Id");
            
            if (userIdStr != null && !userIdStr.isEmpty()) {
                try {
                    Integer userId = Integer.parseInt(userIdStr);
                    
                    // 创建SessionUserInfo对象并设置基本信息
                    SessionUserInfo userInfo = new SessionUserInfo();
                    userInfo.setUserId(userId);
                    
                    // 根据用户ID查询用户名
                    String username = userHeaderService.getUsernameByUserId(userId.longValue());
                    if (username != null) {
                        userInfo.setUsername(username);
                        userInfo.setNickname(username);
                    }
                    
                    log.info("从请求头X-User-Id获取到的用户信息: {}", userInfo.toString());
                    return Result.success(userInfo);
                } catch (NumberFormatException e) {
                    log.warn("X-User-Id格式不正确: {}", userIdStr);
                    return Result.error("X-User-Id格式不正确");
                }
            } else {
                log.warn("请求头中未找到X-User-Id");
                return Result.error("请求头中未找到X-User-Id");
            }
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }
}