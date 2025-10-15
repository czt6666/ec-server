package com.bistu.common.config.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zxh
 * @date: 2023/8/30 15:54
 * @description:
 */
@ConditionalOnExpression("'false'.equals('${webofd.mysql.enable}')")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Configuration
@Slf4j
public class MySqlCloseConfig {

	@Bean
	public void closeDataSourceAutoConfig() {
		log.info("关闭MySQL数据库");
	}
}
