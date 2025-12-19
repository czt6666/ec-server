package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.dao.mapper.UserMapper;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.User;
import com.bistu.ecadmin.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供小程序端登录接口
 */
@RestController
@RequestMapping("/admin/ecadmin/auth")
@Api(tags = "认证管理")
@Slf4j
public class AuthController {

    @Autowired
    private UserMapper userMapper;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    /**
     * 小程序端登录接口
     */
    @PostMapping("/login")
    @ApiOperation("小程序端登录")
    public Result<Map<String, Object>> login(@RequestBody @ApiParam("登录请求，包含username和password") Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            // 验证参数
            if (username == null || username.trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                return Result.error("密码不能为空");
            }

            // 查询用户
            User user = userMapper.selectByUsername(username.trim());
            if (user == null) {
                return Result.error("用户名或密码错误");
            }

            // 验证密码
            String encodedPassword = user.getPassword();
            boolean passwordMatches = false;

            if (passwordEncoder != null) {
                // 使用PasswordEncoder验证（BCrypt等）
                passwordMatches = passwordEncoder.matches(password, encodedPassword);
            } else {
                // 使用MD5验证
                String md5Password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
                passwordMatches = md5Password.equals(encodedPassword);
            }

            if (!passwordMatches) {
                return Result.error("用户名或密码错误");
            }

            // 检查用户状态（deleteStatus = 1 表示正常，!= 1 表示禁用）
            if (user.getDeleteStatus() != null && user.getDeleteStatus() != 1) {
                return Result.error("用户已被禁用");
            }

            // 生成token
            String token = JwtUtil.generateToken(user.getId());

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userId", user.getId());
            data.put("username", user.getUsername());
            data.put("nickname", user.getNickname());

            return Result.success(data);
        } catch (Exception e) {
            log.error("登录失败", e);
            return Result.error("登录失败：" + e.getMessage());
        }
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

