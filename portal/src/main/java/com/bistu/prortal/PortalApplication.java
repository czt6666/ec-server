package com.bistu.prortal;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author: zxh
 * @date: 2024/3/12 11:02
 * @description: 门户系统启动类
 */
@SpringBootApplication
public class PortalApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(PortalApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 注意这里要指向原先用main方法执行的Application启动类
		return builder.sources(PortalApplication.class);
	}

}