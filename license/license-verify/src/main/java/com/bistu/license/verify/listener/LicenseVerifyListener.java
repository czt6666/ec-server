package com.bistu.license.verify.listener;

import com.bistu.license.core.model.LicenseResult;
import com.bistu.license.core.model.LicenseVerifyManager;
import com.bistu.license.core.utils.CommonUtils;
import com.bistu.license.verify.config.LicenseVerifyProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <p>项目启动时安装证书&定时检测lic变化，自动更替lic</p>
 */
@Component
@Slf4j
public class LicenseVerifyListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private LicenseVerifyProperties properties;

    @Autowired
    private ApplicationContext applicationContext;

    /**文件唯一身份标识 == 相当于人类的指纹一样*/
    private static String md5 = "";
    private static boolean isLoad = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getDisplayName().contains("FeignContext")) {
            return;
        }
        if (CommonUtils.isNotEmpty(properties.getLicensePath())) {
            try{
                install();
                String readMd5 = getMd5(properties.getLicensePath());
                isLoad = true;
                if (LicenseVerifyListener.md5 == null || LicenseVerifyListener.md5.isEmpty()) {
                    LicenseVerifyListener.md5 = readMd5;
                }
            } catch (Exception e) {
                SpringApplication.exit(applicationContext, () -> 1);
            }
        }
    }

    /**5秒检测一次，不能太快也不能太慢*/
    /*@Scheduled(cron = "0/5 * * * * ?")
    protected void timer() throws Exception {
        if(!isLoad){
            return;
        }
        String readMd5 = getMd5(properties.getLicensePath());
        // 不相等，说明lic变化了
        if(!readMd5.equals(LicenseVerifyListener.md5)){
            install();
            LicenseVerifyListener.md5 = readMd5;
        }
    }*/

    private void install() throws FileNotFoundException {
        log.info("++++++++ 开始安装证书 ++++++++");
        LicenseVerifyManager licenseVerifyManager = new LicenseVerifyManager();
        /** 走定义校验证书并安装 */
        LicenseResult result = licenseVerifyManager.install(properties.getVerifyParam());
        if(result.getResult()) {
            log.info("++++++++ 证书安装成功 ++++++++");
        } else {
            log.info("++++++++ 开发模式，忽略证书 ++++++++");
//            log.info("++++++++ 证书安装失败 ++++++++");
//            if (result.getMessage().equals("授权文件不存在")) {
//                throw new FileNotFoundException("a");
//            }
        }
    }

    /**
     * <p>获取文件的md5</p>
     */
    public String getMd5(String filePath) throws Exception {
        File file;
        String md5 = "";
        try {
            file = ResourceUtils.getFile(filePath);
            if (file.exists()) {
                FileInputStream is = new FileInputStream(file);
                byte[] data = new byte[is.available()];
                is.read(data);
                md5 = DigestUtils.md5DigestAsHex(data);
                is.close();
            }
        } catch (FileNotFoundException e) {

        }
        return md5;
    }

}
