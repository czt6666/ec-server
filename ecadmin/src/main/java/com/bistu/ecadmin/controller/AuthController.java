package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器（测试版）
 * 统一返回 userId = 1 的 token，方便联调收藏等功能
 */
@RestController
@RequestMapping("/admin/ecadmin/auth")
@Api(tags = "认证管理")
public class AuthController {

    /**
     * 小程序端登录接口（测试环境）
     * 不做用户名密码校验，直接返回 userId=1 的 token
     */
    @PostMapping("/login")
    @ApiOperation("小程序端登录（测试）")
    public Result<Map<String, Object>> login() {
        // 固定使用 userId = 1
        Long userId = 1L;

        // 生成 token
        String token = JwtUtil.generateToken(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", userId);

        return Result.success(data);
    }
}


