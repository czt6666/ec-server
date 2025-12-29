package com.bistu.ecadmin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.access.path}")
    private String accessPath;

    @Value("${file.export.path}")
    private String exportPath;

    @Value("${file.export.access.path}")
    private String exportAccessPath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射
        registry.addResourceHandler(accessPath + "**")
                .addResourceLocations("file:" + uploadPath);
        registry.addResourceHandler(exportAccessPath + "**")
                .addResourceLocations("file:" + exportPath);
    }


    @Override
   public void addInterceptors(InterceptorRegistry registry) {
               registry.addInterceptor(tokenInterceptor)
                               .addPathPatterns("/**")
                                .excludePathPatterns(
                                      "/admin/ecadmin/auth/**",  // 登录接口不拦截
                                       "/swagger-ui.html",        // Swagger UI
                                       "/swagger-resources/**",   // Swagger资源
                                       "/v2/api-docs",            // Swagger API文档
                                       "/webjars/**",              // Swagger静态资源
                                       "/error"                    // 错误页面
                                       );
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                // 需要允许 OPTIONS 以放行带自定义头的预检请求
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);

    }
}
