package com.bistu.license.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>license-core模块中的Bean实现自动装配 -- 配置类</p>
 */
@Configuration
@ComponentScan(basePackages = {"com.bistu.license.core"})
@Slf4j
public class LicenseCoreAutoConfigure {
    public LicenseCoreAutoConfigure(){
        log.info("============ license-core-spring-boot-starter initialization！ ===========");
    }
}
