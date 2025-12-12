package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.DataSource;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DataSourceDao {
    
    List<DataSource> findAll();
    
    DataSource findById(Long id);
    
    int insert(DataSource dataSource);
    
    int update(DataSource dataSource);
    
    int deleteById(Long id);
    
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 根据表名动态查询表记录数
     * @param tableName 表名
     * @return 记录数
     */
    Long countByTableName(@Param("tableName") String tableName);
    
    /**
     * 查找需要同步的数据源（根据同步频率）
     * @param cronExpression cron表达式
     * @return 数据源列表
     */
    List<DataSource> findBySyncFrequency(@Param("cronExpression") String cronExpression);
}