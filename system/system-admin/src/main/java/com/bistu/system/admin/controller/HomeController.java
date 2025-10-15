package com.bistu.system.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.system.MySqlOpenConfig;
import com.bistu.system.admin.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: SJJ
 * @description: 登录相关Controller
 * @date: 2022/6/16
 */
@ConditionalOnBean({MySqlOpenConfig.class})
@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

	@Autowired
	private HomeService homeService;


	/**
	 * 首页
	 */
	@PostMapping("/getHome")
	public JSONObject getHome() {
		return homeService.getHome();
	}

}
