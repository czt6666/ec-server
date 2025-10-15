package com.bistu.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileAppender;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zxh
 * @date: 2024/12/16 10:16
 * @description:
 */
public class FileLogUtil {

	private static final ConcurrentHashMap<String, FileAppender> APPEND_MAP = new ConcurrentHashMap<>();

	private static final String LOG_FILE_PREFIX = "logs/log_";

	private static final String LOG_FILE_SUFFIX = ".log";

	private FileLogUtil() {}

	public static void write(String key, String content) {
		if (key == null || key.isEmpty()) {
			return;
		}
		FileAppender appender = APPEND_MAP.computeIfAbsent(key, k -> {
			File file = new File(LOG_FILE_PREFIX + k + LOG_FILE_SUFFIX);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return new FileAppender(file, StandardCharsets.UTF_8, 1, true);
		});
		synchronized (appender) {
			appender.append(DateUtil.now() + " " + content).flush();
		}
	}
}
