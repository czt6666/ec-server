package com.bistu.system.log.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.system.MySqlOpenConfig;
import com.bistu.common.util.CommonUtil;
import com.bistu.system.log.PO.LogRecordPO;
import com.bistu.system.log.dao.LogDao;
import com.bistu.system.log.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author SJJ
 * @description:
 * @date 2022/8/2
 */
@ConditionalOnBean({MySqlOpenConfig.class})
@Service
@Slf4j
public class LogServiceImpl implements LogService {

	private final LogDao logDao;

	@Value("${file.export.path}")
	private String exportPath;

	@Autowired
	public LogServiceImpl(LogDao logDao) {
		this.logDao = logDao;
	}

	/**
	 * 日志列表
	 */
	public JSONObject listLog(JSONObject jsonObject) {
		CommonUtil.fillPageParam((jsonObject));
		int count = logDao.countLoginLog(jsonObject);
		List<JSONObject> list = logDao.listLoginLog(jsonObject);
		return CommonUtil.successPage(jsonObject, list, count);
	}
	
	/**
	 * 获取登录日志列表（无分页，用于导出功能）
	 */
	private List<JSONObject> getAllLoginLogs(JSONObject jsonObject) {
		System.out.println("进入getAllLoginLogs方法，参数: " + jsonObject);
		try {
			// 直接调用listAllLoginLogs方法获取所有数据，与SQL查询完全一致
			List<JSONObject> logList = logDao.listAllLoginLogs(jsonObject);
			System.out.println("从数据库获取到日志数据数量: " + (logList != null ? logList.size() : 0));
			return logList;
		} catch (Exception e) {
			System.err.println("查询登录日志数据失败: " + e.getMessage());
			e.printStackTrace(); // 打印完整的堆栈跟踪信息
			throw e;
		}
	}

	public JSONObject detailLog(JSONObject jsonObject){
		String list = logDao.detailLog(jsonObject);
		return CommonUtil.successJson(list);
	}

	/**
	 * 操作日志列表
	 */
	public JSONObject listOperateLog(JSONObject jsonObject) {
		CommonUtil.fillPageParam((jsonObject));
		int count = logDao.countOperateLog(jsonObject);
		List<JSONObject> list = logDao.listOperateLog(jsonObject);
		return CommonUtil.successPage(jsonObject, list, count);
	}

	public JSONObject detailOperateLog(Long id){
		String extra = logDao.detailOperateLog(id);
		return CommonUtil.successJson(extra);
	}

	public int insertOperateLog(LogRecordPO logRecordPO){
		return logDao.insertOperateLog(logRecordPO);
	}

	@Override
	public Resource exportLoginLogs(JSONObject jsonObject) {
		try {
			System.out.println("进入exportLoginLogs方法，参数: " + jsonObject);
			log.info("开始导出登录日志，参数: {}", jsonObject);
			
			// 使用与 listLog 方法完全相同的数据获取逻辑
			List<JSONObject> logList = getAllLoginLogs(jsonObject);
			System.out.println("获取到日志数据数量: " + (logList != null ? logList.size() : 0));
			log.info("查询到登录日志列表大小: {}", logList.size());
			
			if (logList == null || logList.isEmpty()) {
				System.out.println("没有找到可导出的日志数据");
				throw new RuntimeException("没有找到要导出的日志数据");
			}

			// 创建导出目录
			File exportDir = new File(exportPath);
			System.out.println("检查导出目录: " + exportPath + ", 是否存在: " + exportDir.exists());
			if (!exportDir.exists()) {
				System.out.println("创建导出目录: " + exportDir.getAbsolutePath());
				boolean created = exportDir.mkdirs();
				System.out.println("目录创建结果: " + created);
				if (!created) {
					throw new RuntimeException("无法创建导出目录: " + exportPath);
				}
			}

			// 生成文件名
			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			String fileName = "登录日志_" + timestamp + ".xlsx";
			String filePath = exportPath + File.separator + fileName;
			System.out.println("准备创建Excel文件: " + filePath);

			// 创建Excel工作簿
			System.out.println("开始创建Excel工作簿...");
			Workbook workbook = new XSSFWorkbook();
			System.out.println("Excel工作簿创建成功");
			Sheet sheet = workbook.createSheet("登录日志");

			// 创建标题行
			Row headerRow = sheet.createRow(0);
			String[] headers = {"日志编号", "登录用户", "登录日期", "校验方式", "执行时长(ms)", "结果"};
			System.out.println("开始设置标题行...");
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				// 设置标题样式
				CellStyle headerStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				headerStyle.setFont(font);
				cell.setCellStyle(headerStyle);
			}
			System.out.println("标题行设置完成");

			// 填充数据 - 使用与 listLog 接口返回数据完全一致的字段映射
			System.out.println("开始填充数据到Excel...");
			for (int i = 0; i < logList.size(); i++) {
				JSONObject log = logList.get(i);
				Row row = sheet.createRow(i + 1);

				// 使用与 /log/listLog 接口完全相同的字段映射，确保Excel导出与前端表格显示一致
				// listLog方法返回的字段映射为: id, creator, date, method, time, name
				// 添加更安全的字段访问，避免空指针异常
				row.createCell(0).setCellValue(log.get("id") != null ? String.valueOf(log.getLongValue("id")) : "");           // 日志编号
				row.createCell(1).setCellValue(log.getString("creator") != null ? log.getString("creator") : ""); // 登录用户
				row.createCell(2).setCellValue(log.getString("date") != null ? log.getString("date") : "");       // 登录日期
				row.createCell(3).setCellValue(log.getString("method") != null ? log.getString("method") : "");    // 校验方式
				row.createCell(4).setCellValue(log.getString("time") != null ? log.getString("time") : "");       // 执行时长(ms)
				row.createCell(5).setCellValue(log.getString("name") != null ? log.getString("name") : "");       // 结果
			}
			System.out.println("数据填充完成");

			// 保存到文件
			FileOutputStream fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
			
			log.info("Excel文件已保存到: {}", filePath);
			System.out.println("Excel文件已保存到: " + filePath);

			// 返回文件资源
			File file = new File(filePath);
			System.out.println("准备返回文件资源: " + file.getAbsolutePath() + ", 文件是否存在: " + file.exists());
			if (!file.exists()) {
				throw new RuntimeException("导出文件创建失败: " + filePath);
			}
			log.info("成功返回文件资源: {}", filePath);
			System.out.println("登录日志导出成功，返回文件资源");
			return new FileSystemResource(file);
		} catch (Exception e) {
			System.err.println("导出登录日志异常: " + e.getMessage());
			e.printStackTrace(); // 打印完整的堆栈跟踪信息
			log.error("导出登录日志失败", e);
			throw new RuntimeException("导出登录日志失败: " + e.getMessage(), e);
		}
	}

}