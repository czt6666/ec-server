package com.bistu.ecadmin.config;

import com.bistu.ecadmin.util.JwtUtil;
import com.bistu.ecadmin.util.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token拦截器
 * 从请求头中提取token，解析用户ID并设置到UserContext中
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String token = request.getHeader(TOKEN_HEADER);
        
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            token = token.substring(TOKEN_PREFIX.length());
        }
        
        // 解析token获取用户ID
        Long userId = null;
        if (token != null && !token.isEmpty()) {
            userId = JwtUtil.getUserIdFromToken(token);
        }
        
        // 设置到UserContext中（如果没有token或token无效，userId为null，表示新用户）
        UserContext.setUserId(userId);
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后清除ThreadLocal，防止内存泄漏
        UserContext.clear();
    }
}











