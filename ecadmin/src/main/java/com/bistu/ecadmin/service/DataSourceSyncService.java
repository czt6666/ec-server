package com.bistu.ecadmin.service;

import com.bistu.ecadmin.dao.DataSourceDao;
import com.bistu.ecadmin.pojo.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataSourceSyncService {
    
    @Autowired
    private DataSourceDao dataSourceDao;
    
    @Autowired
    private DataSourceService dataSourceService;
    
    /**
     * 定时同步所有数据源的记录数（每小时执行一次）
     * 这是一个兜底的同步任务，确保即使个性化同步失败也能定期同步
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void syncAllDataSourceCounts() {
        try {
            List<DataSource> dataSources = dataSourceDao.findAll();
            
            for (DataSource dataSource : dataSources) {
                // 只同步启用的数据源
                if (dataSource.getStatus() == 1 && 
                    dataSource.getTableName() != null && 
                    !dataSource.getTableName().isEmpty()) {
                    
                    try {
                        syncDataSourceInternal(dataSource);
                        System.out.println("已同步数据源: " + dataSource.getName() + 
                                         ", 表名: " + dataSource.getTableName());
                    } catch (Exception e) {
                        System.err.println("同步数据源 " + dataSource.getName() + " 失败: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("同步所有数据源记录数时出错: " + e.getMessage());
        }
    }
    
    /**
     * 每分钟检查并同步需要同步的数据源
     * 支持个性化同步频率
     */
    @Scheduled(cron = "0 * * * * ?") // 每分钟执行一次
    public void syncDataSourcesByFrequency() {
        try {
            // 查找所有启用的数据源
            List<DataSource> dataSources = dataSourceDao.findAll();
            
            for (DataSource dataSource : dataSources) {
                // 只处理启用的数据源
                if (dataSource.getStatus() == 1 && 
                    dataSource.getTableName() != null && 
                    !dataSource.getTableName().isEmpty()) {
                    
                    // 检查是否需要在此时间点同步
                    if (shouldSyncNow(dataSource.getSyncFrequency())) {
                        try {
                            syncDataSourceInternal(dataSource);
                            System.out.println("已按个性化频率同步数据源: " + dataSource.getName() + 
                                             ", 表名: " + dataSource.getTableName() + 
                                             ", 频率: " + dataSource.getSyncFrequency());
                        } catch (Exception e) {
                            System.err.println("按个性化频率同步数据源 " + dataSource.getName() + " 失败: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("按个性化频率同步数据源时出错: " + e.getMessage());
        }
    }
    
    /**
     * 判断当前时间是否应该执行同步
     * @param cronExpression cron表达式
     * @return 是否应该同步
     */
    private boolean shouldSyncNow(String cronExpression) {
        if (cronExpression == null || cronExpression.isEmpty()) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        String[] parts = cronExpression.split(" ");
        
        // 确保cron表达式至少有6个部分
        if (parts.length < 6) {
            return false;
        }
        
        String second = parts[0];
        String minute = parts[1];
        
        // 每分钟同步: * * * * * ?
        // 前端设置的每分钟同步表达式是 "* * * * * ?"
        if (minute.equals("*")) {
            return true;
        }
        
        // 对于其他情况，检查秒字段（应该为0）
        if (!second.equals("0")) {
            return false;
        }
        
        String hour = parts[2];
        
        // 每小时同步: 0 0 * * * ?
        if (minute.equals("0") && hour.equals("*")) {
            return now.getMinute() == 0;
        }
        
        // 每天同步: 0 0 0 * * ?
        if (minute.equals("0") && hour.equals("0")) {
            return now.getMinute() == 0 && now.getHour() == 0;
        }
        
        return false;
    }
    
    /**
     * 同步单个数据源的核心逻辑
     * @param dataSource 数据源对象
     */
    private void syncDataSourceInternal(DataSource dataSource) {
        // 获取实际表记录数
        Long recordCount = dataSourceService.countByTableName(dataSource.getTableName());
        
        // 更新数据源记录数和最后同步时间
        dataSource.setRecordCount(recordCount.intValue());
        dataSource.setLastSyncTime(LocalDateTime.now());
        
        // 更新数据库
        dataSourceDao.update(dataSource);
    }
    
    /**
     * 手动同步单个数据源的记录数
     * @param dataSourceId 数据源ID
     */
    public boolean syncDataSourceCount(Long dataSourceId) {
        try {
            DataSource dataSource = dataSourceDao.findById(dataSourceId);
            
            if (dataSource != null && 
                dataSource.getStatus() == 1 && 
                dataSource.getTableName() != null && 
                !dataSource.getTableName().isEmpty()) {
                
                syncDataSourceInternal(dataSource);
                return true;
            }
        } catch (Exception e) {
            System.err.println("同步数据源 ID " + dataSourceId + " 失败: " + e.getMessage());
        }
        
        return false;
    }
}