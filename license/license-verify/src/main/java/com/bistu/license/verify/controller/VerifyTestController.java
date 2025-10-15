package com.bistu.license.verify.controller;

import com.bistu.license.verify.annotion.VLicense;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>证书认证接口测试</p>
 */
@CrossOrigin
@RestController
@RequestMapping("license")
public class VerifyTestController {

    @VLicense
    @GetMapping("/hello")
    public String sayHello(){
        return "hello license !";
    }

}
