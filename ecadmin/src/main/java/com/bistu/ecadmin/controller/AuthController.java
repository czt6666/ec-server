package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.AuthService;
import com.bistu.ecadmin.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供token解析接口和手机号登录接口
 */
@RestController
@RequestMapping("/admin/ecadmin/auth")
@Api(tags = "认证管理")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;
// AuthController.java 中，类里面其他方法下面加一个

    /**
     * 测试：根据指定 userId 生成 token（仅开发环境使用）
     */
    @GetMapping("/generateTestToken")
    @ApiOperation("测试生成token接口（仅开发调试用）")
    public Result<Map<String, Object>> generateTestToken(@RequestParam Long userId) {
        String token = JwtUtil.generateToken(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("token", token);

        return Result.success(data);
    }
    /**
     * 发送手机验证码
     */
    @PostMapping("/sendSmsCode")
    @ApiOperation("发送手机验证码")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "query")
    public Result<?> sendSmsCode(@RequestParam String phone) {
        return authService.sendSmsCode(phone);
    }

    /**
     * 手机号验证码登录
     */
    @PostMapping("/loginByPhone")
    @ApiOperation("手机号验证码登录")
    public Result<Map<String, Object>> loginByPhone(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String code = params.get("code");

        if (phone == null || phone.trim().isEmpty()) {
            return Result.error("手机号不能为空");
            }
        if (code == null || code.trim().isEmpty()) {
            return Result.error("验证码不能为空");
            }

        return authService.loginByPhone(phone, code);
    }

    /**
     * 用户名密码登录
     */
    @PostMapping("/loginByUsername")
    @ApiOperation("用户名密码登录")
    public Result<Map<String, Object>> loginByUsername(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }

        return authService.loginByUsername(username, password);
    }

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

