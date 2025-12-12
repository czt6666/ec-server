package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.DataSource;
import java.util.List;

public interface DataSourceService {
    List<DataSource> findAll();
    DataSource findById(Long id);
    boolean save(DataSource dataSource);
    boolean update(DataSource dataSource);
    boolean deleteById(Long id);
    boolean updateStatus(Long id, Integer status);
    
    /**
     * 更新数据源同步频率
     * @param id 数据源ID
     * @param frequency 同步频率(cron表达式)
     * @return 是否更新成功
     */
    boolean updateFrequency(Long id, String frequency);
    
    /**
     * 根据表名动态查询表记录数
     * @param tableName 表名
     * @return 记录数
     */
    Long countByTableName(String tableName);
    
    /**
     * 同步单个数据源的记录数
     * @param dataSourceId 数据源ID
     * @return 是否同步成功
     */
    boolean syncDataSourceCount(Long dataSourceId);
    
    /**
     * 同步所有数据源的记录数
     */
    void syncAllDataSourceCounts();
}