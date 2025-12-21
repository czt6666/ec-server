package com.bistu.ecadmin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
public class JwtUtil {

    // 密钥
    private static final String SECRET = "ec-admin-secret-key-2025";

    // Token过期时间（7天）
    private static final long EXPIRATION = 7 * 24 * 60 * 60 * 1000L;

    /**
     * 生成Token
     * @param userId 用户ID
     * @return Token字符串
     */
    public static String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * 从Token中获取用户ID
     * @param token Token字符串
     * @return 用户ID，如果Token无效则返回null
     */
    public static Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            Object userIdObj = claims.get("userId");
            if (userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            } else if (userIdObj != null) {
                return Long.parseLong(userIdObj.toString());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证Token是否有效
     * @param token Token字符串
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}







