package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.mapper.MiniUserMapper;
import com.bistu.ecadmin.pojo.MiniUser;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.AuthService;
import com.bistu.ecadmin.service.SmsService;
import com.bistu.ecadmin.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 认证服务实现类
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private MiniUserMapper miniUserMapper;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder; // 密码加密器（可选）

    @Autowired
    private SmsService smsService; // 短信服务（自动选择：阿里云或简单服务）

    // 验证码缓存：key=手机号，value=验证码信息{code, expireTime}
    private static final ConcurrentHashMap<String, CodeInfo> CODE_CACHE = new ConcurrentHashMap<>();

    // 验证码有效期（5分钟）
    private static final long CODE_EXPIRE_TIME = 5 * 60 * 1000L;

    // 验证码发送间隔（60秒）
    private static final long CODE_SEND_INTERVAL = 60 * 1000L;

    // 手机号正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    // 定时清理过期验证码的线程池
    private static final ScheduledExecutorService CLEANUP_SCHEDULER = Executors.newScheduledThreadPool(1);

    static {
        // 每5分钟清理一次过期验证码
        CLEANUP_SCHEDULER.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            CODE_CACHE.entrySet().removeIf(entry -> entry.getValue().expireTime < now);
        }, 5, 5, TimeUnit.MINUTES);
    }

    /**
     * 验证码信息
     */
    private static class CodeInfo {
        String code;
        long expireTime;
        long sendTime;

        CodeInfo(String code, long expireTime, long sendTime) {
            this.code = code;
            this.expireTime = expireTime;
            this.sendTime = sendTime;
        }
    }

    @Override
    public Result<?> sendSmsCode(String phone) {
        // 验证手机号格式
        if (phone == null || phone.trim().isEmpty()) {
            return Result.error("手机号不能为空");
        }
        phone = phone.trim();
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            return Result.error("手机号格式不正确");
        }

        // 检查发送间隔
        CodeInfo existingCode = CODE_CACHE.get(phone);
        if (existingCode != null) {
            long timeSinceLastSend = System.currentTimeMillis() - existingCode.sendTime;
            if (timeSinceLastSend < CODE_SEND_INTERVAL) {
                long remainingSeconds = (CODE_SEND_INTERVAL - timeSinceLastSend) / 1000;
                return Result.error("验证码发送过于频繁，请" + remainingSeconds + "秒后再试");
            }
        }

        // 生成6位随机验证码
        String code = String.format("%06d", new Random().nextInt(1000000));
        long now = System.currentTimeMillis();
        long expireTime = now + CODE_EXPIRE_TIME;

        // 存储验证码
        CODE_CACHE.put(phone, new CodeInfo(code, expireTime, now));

        // 发送短信验证码
        boolean sendSuccess = smsService.sendSmsCode(phone, code);

        if (!sendSuccess) {
            log.warn("短信发送失败，但验证码已生成：手机号={}, 验证码={}", phone, code);
            // 即使短信发送失败，也返回成功（验证码已生成，用户可以通过其他方式获取）
        }

        return Result.success("验证码已发送");
    }

    @Override
    public Result<Map<String, Object>> loginByPhone(String phone, String code) {
        // 验证手机号格式
        if (phone == null || phone.trim().isEmpty()) {
            return Result.error("手机号不能为空");
        }
        phone = phone.trim();
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            return Result.error("手机号格式不正确");
        }

        // 验证验证码
        if (code == null || code.trim().isEmpty()) {
            return Result.error("验证码不能为空");
        }
        code = code.trim();

        // 支持测试后门验证码（例如输入 "dzk666" 可直接通过验证）
        if ("dzk666".equals(code)) {
            log.warn("使用后门验证码登录（测试用）：phone={}", phone);
        } else {
            CodeInfo codeInfo = CODE_CACHE.get(phone);
            if (codeInfo == null) {
                return Result.error("验证码不存在或已过期，请重新获取");
            }

            // 检查验证码是否过期
            if (System.currentTimeMillis() > codeInfo.expireTime) {
                CODE_CACHE.remove(phone);
                return Result.error("验证码已过期，请重新获取");
            }

            // 验证验证码
            if (!codeInfo.code.equals(code)) {
                return Result.error("验证码错误");
            }

            // 验证码验证成功，删除验证码（防止重复使用）
            CODE_CACHE.remove(phone);
        }

        // 查询或创建小程序用户
        MiniUser miniUser = miniUserMapper.selectByPhone(phone);
        if (miniUser == null) {
            // 用户不存在，创建新小程序用户
            miniUser = new MiniUser();
            miniUser.setPhone(phone);
            miniUser.setNickname("用户" + phone.substring(7)); // 使用手机号后4位作为昵称
            miniUser.setGender(0); // 默认未知
            miniUser.setDeleteStatus(1); // 1表示启用状态
            miniUserMapper.insert(miniUser);
            // 重新查询获取ID
            miniUser = miniUserMapper.selectByPhone(phone);
        } else {
            // 更新最后登录时间
            miniUserMapper.updateLastLoginTime(miniUser.getId());
        }

        // 生成JWT token
        String token = JwtUtil.generateToken(miniUser.getId());

        // 返回登录结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", miniUser.getId());
        result.put("phone", miniUser.getPhone());
        result.put("nickname", miniUser.getNickname());
        result.put("avatar", miniUser.getAvatar());

        log.info("小程序用户登录成功：手机号={}, userId={}", phone, miniUser.getId());

        return Result.success(result);
    }

    @Override
    public Result<Map<String, Object>> loginByUsername(String username, String password) {
        // 验证用户名和密码
        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        username = username.trim();

        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        password = password.trim();

        // 查询用户：优先按用户名查找，若用户名看起来像手机号则尝试按手机号查找
        MiniUser miniUser = miniUserMapper.selectByUsername(username);
        if (miniUser == null && PHONE_PATTERN.matcher(username).matches()) {
            miniUser = miniUserMapper.selectByPhone(username);
        }
        if (miniUser == null) {
            return Result.error("用户名或密码错误");
        }

        // 验证密码
        String storedPassword = miniUser.getPassword();
        if (storedPassword == null || storedPassword.isEmpty()) {
            return Result.error("该用户未设置密码，请使用手机号登录");
        }

        boolean passwordMatch = false;
        if (passwordEncoder != null) {
            // 使用 PasswordEncoder 验证密码（BCrypt等）
            passwordMatch = passwordEncoder.matches(password, storedPassword);
        } else {
            // 使用 MD5 验证密码（简单加密，生产环境建议使用BCrypt）
            String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
            passwordMatch = encryptedPassword.equals(storedPassword);
        }

        if (!passwordMatch) {
            return Result.error("用户名或密码错误");
        }

        // 更新最后登录时间
        miniUserMapper.updateLastLoginTime(miniUser.getId());

        // 生成JWT token
        String token = JwtUtil.generateToken(miniUser.getId());

        // 返回登录结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", miniUser.getId());
        result.put("username", miniUser.getUsername());
        result.put("phone", miniUser.getPhone());
        result.put("nickname", miniUser.getNickname());
        result.put("avatar", miniUser.getAvatar());

        log.info("小程序用户登录成功：用户名={}, userId={}", username, miniUser.getId());

        return Result.success(result);
    }

    @Override
    public Result<?> register(String username, String password, String confirmPassword) {
        // 验证用户名
        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        username = username.trim();

        // 检查是否为手机号格式
        boolean isPhoneFormat = PHONE_PATTERN.matcher(username).matches();

        if (isPhoneFormat) {
            // 如果是手机号格式，长度必须为11位（已在正则中验证）
        } else {
            // 如果不是手机号，验证普通用户名规则
            if (username.length() < 4 || username.length() > 20) {
                return Result.error("用户名长度必须在4-20位之间");
            }
            if (!username.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
                return Result.error("用户名必须以字母开头，只能包含字母、数字和下划线，或使用手机号格式");
            }
        }

        // 验证密码
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        password = password.trim();
        if (password.length() < 6) {
            return Result.error("密码长度不能少于6位");
        }

        // 验证确认密码
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return Result.error("确认密码不能为空");
        }
        confirmPassword = confirmPassword.trim();
        if (!password.equals(confirmPassword)) {
            return Result.error("两次输入的密码不一致");
        }

        // 检查用户名是否已存在
        MiniUser existingUser = miniUserMapper.selectByUsername(username);
        if (existingUser != null) {
            return Result.error("用户名已存在");
        }

        // 如果用户名是手机号格式，额外检查手机号是否已被注册
        if (isPhoneFormat) {
            MiniUser phoneUser = miniUserMapper.selectByPhone(username);
            if (phoneUser != null) {
                return Result.error("该手机号已被注册，请使用其他手机号或用户名");
            }
        }

        // 创建新用户
        MiniUser miniUser = new MiniUser();
        miniUser.setUsername(username);
        miniUser.setPassword(encodePassword(password));

        // 如果用户名是手机号格式，同时设置手机号字段
        if (isPhoneFormat) {
            miniUser.setPhone(username);
            miniUser.setNickname("用户" + username.substring(7)); // 使用手机号后4位作为昵称
        } else {
            // 普通用户名也使用友好格式
            miniUser.setNickname("用户" + username);
        }

        miniUser.setGender(0); // 默认未知性别
        miniUser.setDeleteStatus(1); // 1表示启用状态

        // 保存到数据库
        int result = miniUserMapper.insert(miniUser);
        if (result > 0) {
            log.info("小程序用户注册成功：用户名={}", username);
            return Result.success("注册成功");
        } else {
            log.error("小程序用户注册失败：用户名={}", username);
            return Result.error("注册失败，请稍后再试");
        }
    }

    @Override
    public Result<?> resetPassword(String phone, String code, String newPassword, String confirmPassword) {
        // 验证手机号格式
        if (phone == null || phone.trim().isEmpty()) {
            return Result.error("手机号不能为空");
        }
        phone = phone.trim();
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            return Result.error("手机号格式不正确");
        }

        // 验证验证码
        if (code == null || code.trim().isEmpty()) {
            return Result.error("验证码不能为空");
        }
        code = code.trim();

        // 支持测试后门验证码（例如输入 "dzk666" 可直接通过验证）
        if (!"dzk666".equals(code)) {
            CodeInfo codeInfo = CODE_CACHE.get(phone);
            if (codeInfo == null) {
                return Result.error("验证码不存在或已过期，请重新获取");
            }

            // 检查验证码是否过期
            if (System.currentTimeMillis() > codeInfo.expireTime) {
                CODE_CACHE.remove(phone);
                return Result.error("验证码已过期，请重新获取");
            }

            // 验证验证码
            if (!codeInfo.code.equals(code)) {
                return Result.error("验证码错误");
            }

            // 验证码验证成功，删除验证码（防止重复使用）
            CODE_CACHE.remove(phone);
        }

        // 验证新密码
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.error("新密码不能为空");
        }
        newPassword = newPassword.trim();
        if (newPassword.length() < 6) {
            return Result.error("密码长度不能少于6位");
        }

        // 验证确认密码
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return Result.error("确认密码不能为空");
        }
        confirmPassword = confirmPassword.trim();
        if (!newPassword.equals(confirmPassword)) {
            return Result.error("两次输入的密码不一致");
        }

        // 查找用户（支持手机号和用户名都是手机号的情况）
        MiniUser miniUser = miniUserMapper.selectByPhone(phone);
        if (miniUser == null) {
            // 如果手机号没找到用户，尝试按用户名查找（用户名可能是手机号）
            miniUser = miniUserMapper.selectByUsername(phone);
        }

        if (miniUser == null) {
            return Result.error("用户不存在");
        }

        // 更新密码
        String encodedPassword = encodePassword(newPassword);
        int result = miniUserMapper.updatePassword(miniUser.getId(), encodedPassword);
        if (result > 0) {
            log.info("用户忘记密码重置成功：phone={}, userId={}", phone, miniUser.getId());
            return Result.success("密码重置成功");
        } else {
            log.error("用户忘记密码重置失败：phone={}, userId={}", phone, miniUser.getId());
            return Result.error("密码重置失败，请稍后再试");
        }
    }

    /**
     * 加密密码（用于注册时）
     * @param rawPassword 明文密码
     * @return 加密后的密码
     */
    public String encodePassword(String rawPassword) {
        if (passwordEncoder != null) {
            return passwordEncoder.encode(rawPassword);
        } else {
            // 使用 MD5 加密（简单加密，生产环境建议使用BCrypt）
            return DigestUtils.md5DigestAsHex(rawPassword.getBytes());
        }
    }
}

