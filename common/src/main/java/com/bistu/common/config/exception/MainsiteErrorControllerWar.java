package com.bistu.common.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: SJJ
 * @description: 系统错误拦截, 主要是针对404的错误
 * @date: 2022/6/16
 */
@ConditionalOnExpression("'war'.equals('${webofd.package.type}')")
@Controller
@Slf4j
public class MainsiteErrorControllerWar implements ErrorController {

    private static final String ERROR_PATH = "/error";

    /**
     * war包时对前端请求做转发
     * @return 请求转发
     */
    @RequestMapping(value = {"/webofd", "/webofd/**"})
    public String test1() {
        log.error("======================================");
        return "forward:/";
    }

    /**
     * war包时对前端错误请求做转发
     * @return 请求转发
     */
    @RequestMapping(value = ERROR_PATH)
    public String handleError() {
        log.error("+++++++++++++++++++++++++++++++++++++++");
        return "forward:/";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}

