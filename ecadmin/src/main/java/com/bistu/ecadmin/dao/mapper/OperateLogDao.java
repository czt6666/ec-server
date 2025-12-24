package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OperateLogDao {
    
    /**
     * 插入操作日志
     */
    int insertOperateLog(OperateLog operateLog);
    
    /**
     * 分页查询操作日志
     */
    List<OperateLog> listOperateLogs(@Param("username") String username,
                                    @Param("operation") String operation,
                                    @Param("startTime") java.util.Date startTime,
                                    @Param("endTime") java.util.Date endTime,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);
    
    /**
     * 查询操作日志总数
     */
    int countOperateLogs(@Param("username") String username,
                         @Param("operation") String operation,
                         @Param("startTime") java.util.Date startTime,
                         @Param("endTime") java.util.Date endTime);
    
    /**
     * 根据ID查询操作日志详情
     */
    OperateLog getOperateLogById(@Param("id") Long id);
    
    /**
     * 查询所有匹配的操作日志（用于导出）
     */
    List<OperateLog> listAllOperateLogs(@Param("username") String username,
                                       @Param("operation") String operation,
                                       @Param("startTime") java.util.Date startTime,
                                       @Param("endTime") java.util.Date endTime);
}