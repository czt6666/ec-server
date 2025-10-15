package com.bistu.system.admin.config;

import com.bistu.common.config.system.MySqlOpenConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 模块自动扫包/装配Bean实例</p>
 */

@Configuration
@ComponentScan(basePackages = {"com.bistu.system.login"})
@ConditionalOnBean({MySqlOpenConfig.class})
@MapperScans({@MapperScan("com.bistu.system.login.dao")})
@Slf4j
public class SystemLoginAutoConfig {

    public SystemLoginAutoConfig(){
        log.info("============ com.bistu.system.login initialization！ ===========");
    }
}
