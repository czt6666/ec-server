package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DataSource {
    private Long id;
    private String name;
    private String tableName;
    private Integer status;
    private String syncFrequency; // 同步频率 (cron表达式)
    private LocalDateTime lastSyncTime;
    private Integer recordCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}