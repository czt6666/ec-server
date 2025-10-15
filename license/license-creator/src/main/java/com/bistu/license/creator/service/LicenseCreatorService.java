package com.bistu.license.creator.service;

import com.bistu.license.core.model.LicenseCreatorManager;
import com.bistu.license.core.model.LicenseCreatorParam;
import com.bistu.license.core.model.LicenseResult;
import com.bistu.license.core.result.ResponseResult;
import com.bistu.license.core.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * <p>证书生成接口实现</p>
 */
@Service
public class LicenseCreatorService {
    /**
     * <p>生成证书</p>
     * @param param 证书创建需要的参数对象
     * @return Map<String,Object>
     */
    public ResponseResult generateLicense(LicenseCreatorParam param) {
        LicenseCreatorManager licenseCreator = new LicenseCreatorManager(param);
        LicenseResult licenseResult = licenseCreator.generateLicense();
        if(licenseResult.getResult()){
            String message = MessageFormat.format("证书生成成功，证书有效期：{0} - {1}",
                    DateUtils.date2Str(param.getIssuedTime()), DateUtils.date2Str(param.getExpiryTime()));
            return ResponseResult.ok(message,param);
        }else{
            return ResponseResult.fail("证书文件生成失败！");
        }
    }


}
