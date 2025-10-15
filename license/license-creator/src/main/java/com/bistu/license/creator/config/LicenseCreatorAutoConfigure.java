package com.bistu.license.creator.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>License生成模块自动扫包/装配Bean实例</p>
 */
@Configuration
@ComponentScan(basePackages = {"com.bistu.license.creator"})
@EnableConfigurationProperties({LicenseCreatorProperties.class})
@Slf4j
public class LicenseCreatorAutoConfigure {
    public LicenseCreatorAutoConfigure(){
        log.info("============ license-creator-spring-boot-starter initialization！ ===========");
    }
}
