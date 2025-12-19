package com.bistu.ecadmin.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * ecadmin模块配置类
 */
@Configuration
@EnableAspectJAutoProxy
@MapperScans({@MapperScan("com.bistu.ecadmin.dao")})
@Slf4j
public class EcAdminConfig {

    public EcAdminConfig(){
        log.info("============ com.bistu.ecadmin initialization！ ===========");
    }
}