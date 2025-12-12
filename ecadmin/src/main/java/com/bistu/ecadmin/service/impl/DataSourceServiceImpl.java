package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.DataSourceDao;
import com.bistu.ecadmin.pojo.DataSource;
import com.bistu.ecadmin.service.DataSourceService;
import com.bistu.ecadmin.service.DataSourceSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataSourceServiceImpl implements DataSourceService {
    
    @Autowired
    private DataSourceDao dataSourceDao;
    
    @Autowired
    private DataSourceSyncService dataSourceSyncService;
    
    @Override
    public List<DataSource> findAll() {
        return dataSourceDao.findAll();
    }

    @Override
    public DataSource findById(Long id) {
        return dataSourceDao.findById(id);
    }
    
    @Override
    public boolean save(DataSource dataSource) {
        return dataSourceDao.insert(dataSource) > 0;
    }
    
    @Override
    public boolean update(DataSource dataSource) {
        return dataSourceDao.update(dataSource) > 0;
    }
    
    @Override
    public boolean deleteById(Long id) {
        return dataSourceDao.deleteById(id) > 0;
    }
    
    @Override
    public boolean updateStatus(Long id, Integer status) {
        return dataSourceDao.updateStatus(id, status) > 0;
    }
    
    @Override
    public Long countTodayDataSources() {
        return dataSourceDao.countTodayDataSources();
    }

    @Override
    public boolean updateFrequency(Long id, String frequency) {
        DataSource dataSource = dataSourceDao.findById(id);
        if (dataSource != null) {
            dataSource.setSyncFrequency(frequency);
            return dataSourceDao.update(dataSource) > 0;
        }
        return false;
    }
    
    @Override
    public Long countByTableName(String tableName) {
        try {
            // 使用DataSourceDao的countByTableName方法动态查询表记录数
            return dataSourceDao.countByTableName(tableName);
        } catch (Exception e) {
            // 如果查询出错，返回0
            System.err.println("查询表 " + tableName + " 记录数时出错: " + e.getMessage());
            return 0L;
        }
    }
    
    @Override
    public boolean syncDataSourceCount(Long dataSourceId) {
        return dataSourceSyncService.syncDataSourceCount(dataSourceId);
    }
    
    @Override
    public void syncAllDataSourceCounts() {
        dataSourceSyncService.syncAllDataSourceCounts();
    }
    
    @Override
    public boolean isHealthy() {
        try {
            // 最简单的健康检查：尝试查询数据源数量
            // 如果能成功查询，说明数据库连接正常
            dataSourceDao.findAll();
            return true;
        } catch (Exception e) {
            // 如果查询过程中出现任何异常，认为系统不健康
            return false;
        }
    }
}