/*
package com.bistu.common.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfiguration {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        // Minio 配置。实际项目中，定义到 application.yml 配置文件中
//        String endpoint =  "http://192.168.160.15:9000";
//        String accessKey =  "minioadmin";
//        String secretKey = "minioadmin";
//        String endpoint =  "http://127.0.0.1:9000";
//        String accessKey =  "admin";
//        String secretKey = "password";

        // 创建 MinioClient 客户端
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

}*/
