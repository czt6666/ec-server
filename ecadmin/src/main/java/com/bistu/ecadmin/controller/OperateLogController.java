package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.OperateLog;
import com.bistu.ecadmin.service.OperateLogService;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/operateLog")
public class OperateLogController {
    
    @Autowired
    private OperateLogService operateLogService;
    
    /**
     * 分页查询操作日志
     */
    @GetMapping("/list")
    public Result listOperateLogs(@RequestParam(required = false) String username,
                                  @RequestParam(required = false) String operation,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        
        List<OperateLog> logs = operateLogService.listOperateLogs(username, operation, startTime, endTime, page, size);
        int total = operateLogService.countOperateLogs(username, operation, startTime, endTime);
        
        PageResult<OperateLog> pageResult = new PageResult<>();
        pageResult.setRecords(logs);
        pageResult.setTotal(total);
        
        return Result.success(pageResult);
    }
    
    /**
     * 根据ID查询操作日志详情
     */
    @GetMapping("/detail/{id}")
    public Result getOperateLogDetail(@PathVariable Long id) {
        OperateLog log = operateLogService.getOperateLogById(id);
        if (log != null) {
            return Result.success(log);
        } else {
            return Result.error("未找到对应的操作日志");
        }
    }
}