package com.bistu.common.config.system;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author: zxh
 * @date: 2023/8/30 16:09
 * @description:
 */
@ConditionalOnExpression("'true'.equals('${webofd.mysql.enable}')")
@Configuration
public class MySqlOpenConfig {

	@Bean
	public void openDataSourceAutoConfig() {
		System.out.println("开启MySQL数据库");
	}
}
