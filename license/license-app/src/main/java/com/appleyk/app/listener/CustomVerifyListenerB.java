package com.appleyk.app.listener;

import com.bistu.license.core.ex.CommonException;
import com.bistu.license.core.model.LicenseExtraParam;
import com.bistu.license.verify.listener.ACustomVerifyListener;
import org.springframework.stereotype.Component;

/**
 * <p>Lic自定义验证监听器B</p>
 */
@Component
public class CustomVerifyListenerB extends ACustomVerifyListener {
    @Override
    public boolean verify(LicenseExtraParam licenseExtra) throws CommonException {
        System.out.println("======= 自定义证书验证监听器B 实现verify方法  =======");
        return true;
    }
}
