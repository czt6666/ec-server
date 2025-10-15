package com.bistu.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>License验证模块自动扫包/装配Bean实例</p>
 */
@Configuration
@ComponentScan(basePackages = {"com.bistu.common"})
@Slf4j
public class CommonAutoConfig {

    public CommonAutoConfig(){
        log.info("============ com.bistu.common initialization！ ===========");
    }
}
