package com.bistu.system.log.service.impl;

import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.bistu.system.log.dao.WebOFDLogDO;
import com.bistu.system.log.dao.LogRecordType;
import com.bistu.system.log.service.WebOFDLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author lenovo
 */
@Service
public class WebOFDLogServiceImpl implements WebOFDLogService {
    private final Logger businessLog = LoggerFactory.getLogger("businessLog");


    @Override
    @LogRecord(
            fail = "打开文件失败，失败原因：「{{#_errorMsg}}」",
            subType = "打开文件",
            extra = "{{#webOFDLogDO.toString()}}",
            success = "{{#webOFDLogDO.creator}}打开了一个{{#webOFDLogDO.suffix}},文件ID为「{{#webOFDLogDO.fileId}}」,执行结果:{{#_ret}}「{{#failMsg}}」",
            type = LogRecordType.FILE, bizNo = "{{#webOFDLogDO.fileId}}")
    public boolean openFileLog(WebOFDLogDO webOFDLogDO) {
        businessLog.info("【打开文件】fileId={}", webOFDLogDO.getFileId());
        String resInfo = "成功";
        LogRecordContext.putVariable("failMsg", resInfo);
        if (webOFDLogDO.getErrorMsg() != null){
            resInfo = webOFDLogDO.getErrorMsg();
            LogRecordContext.putVariable("failMsg", resInfo);
            /* 2024-05-31 zxh-update 修改抛出异常为返回false */
            // throw new RuntimeException(resInfo);
            return false;
        }
        return true;
    }
}
