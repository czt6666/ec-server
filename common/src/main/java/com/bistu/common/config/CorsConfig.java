package com.bistu.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // 已禁用：CORS配置已迁移到 ecadmin 模块的 WebMvcConfig
    // 使用 allowedOrigins("*") 和 allowCredentials(true) 的组合是无效的
    // 现在统一使用 ecadmin/src/main/java/com/bistu/ecadmin/config/WebMvcConfig.java 中的配置
    
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**").allowedOrigins("*")
    //             .allowedMethods("GET", "HEAD", "POST","PUT", "DELETE", "OPTIONS")
    //             .allowCredentials(true).maxAge(3600);
    // }
}

