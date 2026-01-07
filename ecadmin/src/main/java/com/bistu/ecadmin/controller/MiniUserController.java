package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.dao.mapper.MiniUserMapper;
import com.bistu.ecadmin.pojo.MiniUser;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.AuthService;
import com.bistu.ecadmin.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序用户管理控制器
 */
@RestController
@RequestMapping("/admin/ecadmin/miniUser")
@Api(tags = "小程序用户管理")
@Slf4j
public class MiniUserController {

    // 手机号正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Autowired
    private MiniUserMapper miniUserMapper;

    @Autowired
    private AuthService authService;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    /**
     * 获取当前登录用户的信息
     */
    @GetMapping("/info")
    @ApiOperation("获取当前用户信息")
    public Result<Map<String, Object>> getCurrentUserInfo(HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            if (userId == null) {
                return Result.error("用户未登录");
            }

            MiniUser miniUser = miniUserMapper.selectById(userId);
            if (miniUser == null) {
                return Result.error("用户不存在");
            }

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", miniUser.getId());
            userInfo.put("phone", miniUser.getPhone());
            userInfo.put("username", miniUser.getUsername());
            userInfo.put("nickname", miniUser.getNickname());
            userInfo.put("avatar", miniUser.getAvatar());
            userInfo.put("gender", miniUser.getGender());
            userInfo.put("createTime", miniUser.getCreateTime());
            userInfo.put("lastLoginTime", miniUser.getLastLoginTime());

            return Result.success(userInfo);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    @ApiOperation("更新用户信息")
    public Result<?> updateUserInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            if (userId == null) {
                return Result.error("用户未登录");
            }

            MiniUser miniUser = miniUserMapper.selectById(userId);
            if (miniUser == null) {
                return Result.error("用户不存在");
            }

            // 更新昵称
            if (params.containsKey("nickname")) {
                String nickname = (String) params.get("nickname");
                if (nickname != null && !nickname.trim().isEmpty()) {
                    miniUser.setNickname(nickname.trim());
                }
            }

            // 更新性别
            if (params.containsKey("gender")) {
                Integer gender = (Integer) params.get("gender");
                if (gender != null && (gender == 0 || gender == 1 || gender == 2)) {
                    miniUser.setGender(gender);
                }
            }

            int result = miniUserMapper.update(miniUser);
            if (result > 0) {
                return Result.success("用户信息更新成功");
            } else {
                return Result.error("用户信息更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error("更新用户信息失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户名
     */
    @PutMapping("/updateUsername")
    @ApiOperation("更新用户名")
    public Result<?> updateUsername(@RequestBody Map<String, String> params, HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            if (userId == null) {
                return Result.error("用户未登录");
            }

            String newUsername = params.get("username");
            if (newUsername == null || newUsername.trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }

            newUsername = newUsername.trim();

            // 检查用户名格式：支持手机号格式或普通用户名格式
            boolean isPhoneFormat = PHONE_PATTERN.matcher(newUsername).matches();
            boolean isValidUsername = newUsername.matches("^[a-zA-Z][a-zA-Z0-9_]{3,19}$");

            if (!isPhoneFormat && !isValidUsername) {
                return Result.error("用户名格式不正确：手机号格式或字母开头包含字母数字下划线4-20位");
            }

            // 检查用户名是否已被使用
            MiniUser existingUser = miniUserMapper.selectByUsername(newUsername);
            if (existingUser != null && !existingUser.getId().equals(userId)) {
                return Result.error("用户名已被使用");
            }

            MiniUser miniUser = miniUserMapper.selectById(userId);
            if (miniUser == null) {
                return Result.error("用户不存在");
            }

            miniUser.setUsername(newUsername);
            int result = miniUserMapper.update(miniUser);

            if (result > 0) {
                return Result.success("用户名更新成功");
            } else {
                return Result.error("用户名更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户名失败", e);
            return Result.error("更新用户名失败：" + e.getMessage());
        }
    }

    /**
     * 更新密码
     */
    @PutMapping("/updatePassword")
    @ApiOperation("更新密码")
    public Result<?> updatePassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            if (userId == null) {
                return Result.error("用户未登录");
            }

            String oldPassword = params.get("oldPassword");
            String newPassword = params.get("newPassword");

            if (newPassword == null || newPassword.trim().isEmpty()) {
                return Result.error("新密码不能为空");
            }

            newPassword = newPassword.trim();
            if (newPassword.length() < 6) {
                return Result.error("密码长度不能少于6位");
            }

            MiniUser miniUser = miniUserMapper.selectById(userId);
            if (miniUser == null) {
                return Result.error("用户不存在");
            }

            // 检查旧密码（如果用户设置了密码）
            String storedPassword = miniUser.getPassword();
            if (storedPassword != null && !storedPassword.isEmpty()) {
                if (oldPassword == null || oldPassword.trim().isEmpty()) {
                    return Result.error("请输入当前密码");
                }

                boolean oldPasswordMatch = false;
                if (passwordEncoder != null) {
                    oldPasswordMatch = passwordEncoder.matches(oldPassword.trim(), storedPassword);
                } else {
                    String encryptedOldPassword = DigestUtils.md5DigestAsHex(oldPassword.trim().getBytes());
                    oldPasswordMatch = encryptedOldPassword.equals(storedPassword);
                }

                if (!oldPasswordMatch) {
                    return Result.error("当前密码错误");
                }
            }

            // 加密新密码
            String encryptedNewPassword;
            if (passwordEncoder != null) {
                encryptedNewPassword = passwordEncoder.encode(newPassword);
            } else {
                encryptedNewPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
            }

            miniUser.setPassword(encryptedNewPassword);
            int result = miniUserMapper.update(miniUser);

            if (result > 0) {
                return Result.success("密码更新成功");
            } else {
                return Result.error("密码更新失败");
            }
        } catch (Exception e) {
            log.error("更新密码失败", e);
            return Result.error("更新密码失败：" + e.getMessage());
        }
    }

    /**
     * 更新手机号
     */
    @PutMapping("/updatePhone")
    @ApiOperation("更新手机号")
    public Result<?> updatePhone(@RequestBody Map<String, String> params, HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            if (userId == null) {
                return Result.error("用户未登录");
            }

            String newPhone = params.get("phone");

            if (newPhone == null || newPhone.trim().isEmpty()) {
                return Result.error("手机号不能为空");
            }

            newPhone = newPhone.trim();

            // 验证手机号格式
            if (!PHONE_PATTERN.matcher(newPhone).matches()) {
                return Result.error("手机号格式不正确");
            }

            // 检查手机号是否已被使用
            MiniUser existingUser = miniUserMapper.selectByPhone(newPhone);
            if (existingUser != null && !existingUser.getId().equals(userId)) {
                return Result.error("手机号已被使用");
            }

            MiniUser miniUser = miniUserMapper.selectById(userId);
            if (miniUser == null) {
                return Result.error("用户不存在");
            }

            miniUser.setPhone(newPhone);
            int result = miniUserMapper.update(miniUser);

            if (result > 0) {
                return Result.success("手机号更新成功");
            } else {
                return Result.error("手机号更新失败");
            }
        } catch (Exception e) {
            log.error("更新手机号失败", e);
            return Result.error("更新手机号失败：" + e.getMessage());
        }
    }

    /**
     * 更新头像
     */
    @PutMapping("/updateAvatar")
    @ApiOperation("更新头像")
    public Result<?> updateAvatar(@RequestBody Map<String, String> params, HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            if (userId == null) {
                return Result.error("用户未登录");
            }

            String avatarUrl = params.get("avatar");
            if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
                return Result.error("头像URL不能为空");
            }

            MiniUser miniUser = miniUserMapper.selectById(userId);
            if (miniUser == null) {
                return Result.error("用户不存在");
            }

            miniUser.setAvatar(avatarUrl.trim());
            int result = miniUserMapper.update(miniUser);

            if (result > 0) {
                return Result.success("头像更新成功");
            } else {
                return Result.error("头像更新失败");
            }
        } catch (Exception e) {
            log.error("更新头像失败", e);
            return Result.error("更新头像失败：" + e.getMessage());
        }
    }

    /**
     * 删除用户（软删除）
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除用户")
    public Result<?> deleteUser(@RequestBody Map<String, String> params, HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            if (userId == null) {
                return Result.error("用户未登录");
            }

            String password = params.get("password");
            if (password == null || password.trim().isEmpty()) {
                return Result.error("请输入密码确认删除");
            }

            MiniUser miniUser = miniUserMapper.selectById(userId);
            if (miniUser == null) {
                return Result.error("用户不存在");
            }

            // 验证密码
            String storedPassword = miniUser.getPassword();
            if (storedPassword != null && !storedPassword.isEmpty()) {
                boolean passwordMatch = false;
                if (passwordEncoder != null) {
                    passwordMatch = passwordEncoder.matches(password.trim(), storedPassword);
                } else {
                    String encryptedPassword = DigestUtils.md5DigestAsHex(password.trim().getBytes());
                    passwordMatch = encryptedPassword.equals(storedPassword);
                }

                if (!passwordMatch) {
                    return Result.error("密码错误");
                }
            }

            // 软删除用户
            int result = miniUserMapper.deleteById(userId);

            if (result > 0) {
                return Result.success("用户删除成功");
            } else {
                return Result.error("用户删除失败");
            }
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return Result.error("删除用户失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.trim().isEmpty()) {
            return null;
        }

        String token = authorization.trim();
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        return JwtUtil.getUserIdFromToken(token);
    }
}
