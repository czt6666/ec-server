package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.OperateLog;
import org.springframework.core.io.Resource;
import java.util.List;

public interface OperateLogService {
    
    /**
     * 保存操作日志
     */
    int saveOperateLog(OperateLog operateLog);
    
    /**
     * 分页查询操作日志
     */
    List<OperateLog> listOperateLogs(String username, String operation, 
                                   java.util.Date startTime, java.util.Date endTime, 
                                   int page, int size);
    
    /**
     * 查询操作日志总数
     */
    int countOperateLogs(String username, String operation, 
                        java.util.Date startTime, java.util.Date endTime);
    
    /**
     * 根据ID查询操作日志详情
     */
    OperateLog getOperateLogById(Long id);
    
    /**
     * 导出操作日志
     */
    Resource exportOperateLogs(String username, String operation, 
                              java.util.Date startTime, java.util.Date endTime) throws Exception;
}