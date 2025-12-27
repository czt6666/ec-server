package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供token解析接口（小程序端使用token进行身份验证，不需要登录接口）
 */
@RestController
@RequestMapping("/admin/ecadmin/auth")
@Api(tags = "认证管理")
@Slf4j
public class AuthController {

    /**
     * 小程序端登录接口已移除
     * 小程序端直接使用token进行身份验证，token由其他方式生成
     */

    /**
     * 解析 token 测试接口
     * 前端在请求头传 Authorization: Bearer xxx 或直接传 token 都可以
     */
    @GetMapping("/parseToken")
    @ApiOperation("解析 token 测试接口")
    public Result<Map<String, Object>> parseToken(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        Map<String, Object> data = new HashMap<>();

        if (authorizationHeader == null || authorizationHeader.trim().isEmpty()) {
            data.put("userId", null);
            data.put("valid", false);
            data.put("message", "请求头未携带 Authorization");
            return Result.success(data);
        }

        String token = authorizationHeader.trim();
        
        // 去除 Bearer 前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        
        // 去除可能的花括号和引号（处理复制粘贴时的格式问题）
        token = token.replaceAll("^[{\"']+", "").replaceAll("[}\"']+$", "");
        token = token.trim();

        Long userId = JwtUtil.getUserIdFromToken(token);
        boolean valid = userId != null && JwtUtil.validateToken(token);

        data.put("userId", userId);
        data.put("valid", valid);
        data.put("rawToken", token);

        if (!valid) {
            data.put("message", "token 无效或已过期");
        } else {
            data.put("message", "token 解析成功");
        }

        return Result.success(data);
    }
}

