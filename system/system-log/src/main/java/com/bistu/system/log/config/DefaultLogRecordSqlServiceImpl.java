package com.bistu.system.log.config;

import com.bistu.system.log.PO.LogRecordPO;
import com.bistu.system.log.service.LogService;
import com.google.common.collect.Lists;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.service.ILogRecordService;
import com.bistu.common.config.system.MySqlOpenConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lenovo
 */
@ConditionalOnBean({MySqlOpenConfig.class})
@ComponentScan(basePackages = {"com.bistu.system.log"})
@MapperScans({@MapperScan("com.bistu.system.log.dao")})
@Component
@Slf4j
public class DefaultLogRecordSqlServiceImpl implements ILogRecordService {
	@Value("${webofd.mysql.enable}")
	private String isSQLDatabaseOp;
	private static final Logger businessLog = LoggerFactory.getLogger("businessLog");
	@Autowired(required = false)
	private LogService logService;

	private final List<LogRecord> logRecordList = Lists.newArrayList();

	public DefaultLogRecordSqlServiceImpl() {
	}

	@Override
	public void record(LogRecord logRecord) {
		businessLog.info("【logRecord】log={}", logRecord);
		if ("true".equals(isSQLDatabaseOp)) {
			logService.insertOperateLog(LogRecordPO.from(logRecord));
		}
//        logRecordList.add(logRecord);
	}

	@Override
	public List<LogRecord> queryLog(String bizNo, String type) {
		return logRecordList;
	}

	@Override
	public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
		return new ArrayList<>();
	}
}
