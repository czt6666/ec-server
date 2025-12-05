package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.ElderlyServiceMode;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ElderlyServiceModeDao {
    
    /**
     * 插入新的养老服务模式
     * @param elderlyServiceMode 养老服务模式对象
     * @return 插入影响的行数
     */
    @Insert("INSERT INTO elderly_service_mode(mode_name, sort, status, create_time) " +
            "VALUES(#{modeName}, #{sort}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ElderlyServiceMode elderlyServiceMode);
    
    /**
     * 根据ID查询养老服务模式
     * @param id 养老服务模式ID
     * @return 养老服务模式对象
     */
    @Select("SELECT id, mode_name as modeName, sort, status, create_time as createTime FROM elderly_service_mode WHERE id = #{id}")
    ElderlyServiceMode selectById(Long id);
    
    /**
     * 查询养老服务模式列表
     * @param modeName 模式名称（模糊查询）
     * @param status 状态
     * @return 养老服务模式列表
     */
    @Select("<script>" +
            "SELECT id, mode_name as modeName, sort, status, create_time as createTime FROM elderly_service_mode " +
            "<where>" +
            "<if test='modeName != null and modeName != \"\"'>" +
            "AND mode_name LIKE CONCAT('%', #{modeName}, '%')" +
            "</if>" +
            "<if test='status != null'>" +
            "AND status = #{status}" +
            "</if>" +
            "</where>" +
            "ORDER BY sort ASC, create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<ElderlyServiceMode> selectList(@Param("modeName") String modeName, @Param("status") Integer status, 
                                      @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 查询养老服务模式总数
     * @param modeName 模式名称（模糊查询）
     * @param status 状态
     * @return 总数
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM elderly_service_mode " +
            "<where>" +
            "<if test='modeName != null and modeName != \"\"'>" +
            "AND mode_name LIKE CONCAT('%', #{modeName}, '%')" +
            "</if>" +
            "<if test='status != null'>" +
            "AND status = #{status}" +
            "</if>" +
            "</where>" +
            "</script>")
    int count(@Param("modeName") String modeName, @Param("status") Integer status);
    
    /**
     * 更新养老服务模式
     * @param elderlyServiceMode 养老服务模式对象
     * @return 更新影响的行数
     */
    @Update("<script>" +
            "UPDATE elderly_service_mode " +
            "<set>" +
            "<if test='modeName != null and modeName != \"\"'>mode_name = #{modeName},</if>" +
            "<if test='sort != null'>sort = #{sort},</if>" +
            "<if test='status != null'>status = #{status},</if>" +
            "create_time = NOW()" +
            "</set>" +
            "WHERE id = #{id}" +
            "</script>")
    int update(ElderlyServiceMode elderlyServiceMode);
    
    /**
     * 根据ID删除养老服务模式
     * @param id 养老服务模式ID
     * @return 删除影响的行数
     */
    @Delete("DELETE FROM elderly_service_mode WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 更新养老服务模式状态
     * @param id 养老服务模式ID
     * @param status 状态
     * @return 更新影响的行数
     */
    @Update("UPDATE elderly_service_mode SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 根据模式名称查询（用于唯一性校验）
     * @param modeName 模式名称
     * @return 养老服务模式对象
     */
    @Select("SELECT id, mode_name as modeName, sort, status, create_time as createTime FROM elderly_service_mode WHERE mode_name = #{modeName}")
    ElderlyServiceMode selectByModeName(String modeName);
}