package com.bistu.license.verify.interceptor;

import com.bistu.common.config.exception.CommonJsonException;
import com.bistu.common.util.constants.ErrorEnum;
import com.bistu.license.core.ex.CommonException;
import com.bistu.license.core.model.LicenseExtraParam;
import com.bistu.license.core.model.LicenseResult;
import com.bistu.license.core.model.LicenseVerifyManager;
import com.bistu.license.core.result.ResultCode;
import com.bistu.license.core.utils.CommonUtils;
import com.bistu.license.verify.annotion.VLicense;
import com.bistu.license.verify.config.LicenseVerifyProperties;
import com.bistu.license.verify.listener.ACustomVerifyListener;
import de.schlichtherle.license.LicenseContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>License验证拦截器</p>
 */
public class LicenseVerifyInterceptor implements HandlerInterceptor {

    @Autowired
    private LicenseVerifyProperties properties;

    public LicenseVerifyInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            VLicense annotation = method.getAnnotation(VLicense.class);
            if (CommonUtils.isNotEmpty(annotation)) {
                LicenseVerifyManager licenseVerifyManager = new LicenseVerifyManager();
                /** 1、校验证书是否有效 */
                LicenseResult verifyResult = licenseVerifyManager.verify(properties.getVerifyParam());
                if(!verifyResult.getResult()){
                    throw new CommonJsonException(ErrorEnum.E_601);
                }
                LicenseContent content = verifyResult.getContent();
                LicenseExtraParam licenseCheck = (LicenseExtraParam) content.getExtra();
                if (verifyResult.getResult()) {
                    /** 增加业务系统监听，是否自定义验证 */
                    List<ACustomVerifyListener> customListenerList = ACustomVerifyListener.getCustomListenerList();
                    boolean compare = true;
                    for (ACustomVerifyListener listener : customListenerList) {
                        boolean verify = listener.verify(licenseCheck);
                        compare = compare && verify;
                    }
                    return compare;
                }
                throw new CommonException(ResultCode.FAIL,verifyResult.getException().getMessage());
            }
        }
        return true;
    }
}
