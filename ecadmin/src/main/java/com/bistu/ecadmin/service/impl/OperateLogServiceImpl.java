package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.mapper.OperateLogDao;
import com.bistu.ecadmin.pojo.OperateLog;
import com.bistu.ecadmin.service.OperateLogService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OperateLogServiceImpl implements OperateLogService {
    
    @Autowired
    private OperateLogDao operateLogDao;
    
    @Value("${file.export.path}")
    private String exportPath;
    
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
    
    @Override
    public Resource exportOperateLogs(String username, String operation, 
                                     java.util.Date startTime, java.util.Date endTime) throws Exception {
        // 获取所有匹配的日志记录
        List<OperateLog> logs = operateLogDao.listAllOperateLogs(username, operation, startTime, endTime);
        
        // 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("操作日志");
        
        // 创建表头样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        // 表头
        String[] headers = {"日志编号", "操作日期", "操作人", "操作类型", "请求方法", "执行结果", "耗时(ms)", "IP地址", "操作用户ID", "请求参数", "响应结果", "错误信息"};
        Row headerRow = sheet.createRow(0);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // 填充数据
        for (int i = 0; i < logs.size(); i++) {
            OperateLog log = logs.get(i);
            Row row = sheet.createRow(i + 1);
            
            int colIndex = 0;
            row.createCell(colIndex++).setCellValue(log.getId() != null ? log.getId().toString() : "");
            row.createCell(colIndex++).setCellValue(log.getCreateTime() != null ? log.getCreateTime().toString() : "");
            row.createCell(colIndex++).setCellValue(log.getUsername() != null ? log.getUsername() : "");
            row.createCell(colIndex++).setCellValue(log.getOperation() != null ? log.getOperation() : "");
            row.createCell(colIndex++).setCellValue(log.getMethod() != null ? log.getMethod() : "");
            row.createCell(colIndex++).setCellValue(log.getSuccess() != null ? (log.getSuccess() == 1 ? "成功" : "失败") : "");
            row.createCell(colIndex++).setCellValue(log.getCostTime() != null ? log.getCostTime().toString() : "");
            row.createCell(colIndex++).setCellValue(log.getIpAddress() != null ? log.getIpAddress() : "");
            row.createCell(colIndex++).setCellValue(log.getUserId() != null ? log.getUserId().toString() : "");
            row.createCell(colIndex++).setCellValue(log.getRequestParams() != null ? log.getRequestParams() : "");
            row.createCell(colIndex++).setCellValue(log.getResponseResult() != null ? log.getResponseResult() : "");
            row.createCell(colIndex++).setCellValue(log.getErrorMessage() != null ? log.getErrorMessage() : "");
        }
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // 确保导出目录存在
        File exportDir = new File(exportPath);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        
        // 生成文件名 - 确保时间戳格式正确
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = "操作日志_" + timestamp + ".xlsx";
        String filePath = exportPath + File.separator + fileName;
        
        // 保存到文件
        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        
        // 返回文件资源
        File file = new File(filePath);
        return new FileSystemResource(file);
    }
}