package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.mapper.OperateLogDao;
import com.bistu.ecadmin.pojo.OperateLog;
import com.bistu.ecadmin.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OperateLogServiceImpl implements OperateLogService {
    
    @Autowired
    private OperateLogDao operateLogDao;
    
    @Override
    public int saveOperateLog(OperateLog operateLog) {
        return operateLogDao.insertOperateLog(operateLog);
    }
    
    @Override
    public List<OperateLog> listOperateLogs(String username, String operation, 
                                          java.util.Date startTime, java.util.Date endTime, 
                                          int page, int size) {
        int offset = (page - 1) * size;
        return operateLogDao.listOperateLogs(username, operation, startTime, endTime, offset, size);
    }
    
    @Override
    public int countOperateLogs(String username, String operation, 
                               java.util.Date startTime, java.util.Date endTime) {
        return operateLogDao.countOperateLogs(username, operation, startTime, endTime);
    }
    
    @Override
    public OperateLog getOperateLogById(Long id) {
        return operateLogDao.getOperateLogById(id);
    }
}