package com.bistu.license.verify.config;

import com.bistu.license.core.model.LicenseVerifyParam;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>License验证属性类</p>
 */
@Getter
@Component
@ConfigurationProperties(prefix = "springboot.license.verify")
public class LicenseVerifyProperties {

    private String subject;
    private String publicAlias;
    private String publicKeysStorePath = "";
    private String storePass = "";
    private String licensePath;

    public LicenseVerifyProperties() {
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPublicAlias(String publicAlias) {
        this.publicAlias = publicAlias;
    }

    public void setPublicKeysStorePath(String publicKeysStorePath) {
        this.publicKeysStorePath = publicKeysStorePath;
    }

    public void setStorePass(String storePass) {
        this.storePass = storePass;
    }

    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }

    public LicenseVerifyParam getVerifyParam() {
        LicenseVerifyParam param = new LicenseVerifyParam();
        param.setSubject(subject);
        param.setPublicAlias(publicAlias);
        param.setStorePass(storePass);
        param.setLicensePath(licensePath);
        param.setPublicKeysStorePath(publicKeysStorePath);
        return param;
    }
}
