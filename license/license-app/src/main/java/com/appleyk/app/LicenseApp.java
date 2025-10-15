package com.appleyk.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * <p>SpringBoot启动类</p>
 */
@SpringBootApplication
public class LicenseApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LicenseApp.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LicenseApp.class);
    }
}
