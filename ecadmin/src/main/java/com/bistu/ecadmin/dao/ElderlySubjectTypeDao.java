package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.ElderlySubjectType;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ElderlySubjectTypeDao {
    
    /**
     * 插入新的养老服务主体类型
     * @param elderlySubjectType 养老服务主体类型对象
     * @return 插入影响的行数
     */
    @Insert("INSERT INTO elderly_subject_type(type_name, sort, status, create_time) " +
            "VALUES(#{typeName}, #{sort}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ElderlySubjectType elderlySubjectType);
    
    /**
     * 根据ID查询养老服务主体类型
     * @param id 养老服务主体类型ID
     * @return 养老服务主体类型对象
     */
    @Select("SELECT id, type_name as typeName, sort, status, create_time as createTime FROM elderly_subject_type WHERE id = #{id}")
    ElderlySubjectType selectById(Long id);
    
    /**
     * 查询养老服务主体类型列表
     * @param typeName 类型名称（模糊查询）
     * @param status 状态
     * @return 养老服务主体类型列表
     */
    @Select("<script>" +
            "SELECT id, type_name as typeName, sort, status, create_time as createTime FROM elderly_subject_type " +
            "<where>" +
            "<if test='typeName != null and typeName != \"\"'>" +
            "AND type_name LIKE CONCAT('%', #{typeName}, '%')" +
            "</if>" +
            "<if test='status != null'>" +
            "AND status = #{status}" +
            "</if>" +
            "</where>" +
            "ORDER BY sort ASC, create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<ElderlySubjectType> selectList(@Param("typeName") String typeName, @Param("status") Integer status, 
                                      @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 查询养老服务主体类型总数
     * @param typeName 类型名称（模糊查询）
     * @param status 状态
     * @return 总数
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM elderly_subject_type " +
            "<where>" +
            "<if test='typeName != null and typeName != \"\"'>" +
            "AND type_name LIKE CONCAT('%', #{typeName}, '%')" +
            "</if>" +
            "<if test='status != null'>" +
            "AND status = #{status}" +
            "</if>" +
            "</where>" +
            "</script>")
    int count(@Param("typeName") String typeName, @Param("status") Integer status);
    
    /**
     * 更新养老服务主体类型
     * @param elderlySubjectType 养老服务主体类型对象
     * @return 更新影响的行数
     */
    @Update("<script>" +
            "UPDATE elderly_subject_type " +
            "<set>" +
            "<if test='typeName != null and typeName != \"\"'>type_name = #{typeName},</if>" +
            "<if test='sort != null'>sort = #{sort},</if>" +
            "<if test='status != null'>status = #{status},</if>" +
            "create_time = NOW()" +
            "</set>" +
            "WHERE id = #{id}" +
            "</script>")
    int update(ElderlySubjectType elderlySubjectType);
    
    /**
     * 根据ID删除养老服务主体类型
     * @param id 养老服务主体类型ID
     * @return 删除影响的行数
     */
    @Delete("DELETE FROM elderly_subject_type WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 更新养老服务主体类型状态
     * @param id 养老服务主体类型ID
     * @param status 状态
     * @return 更新影响的行数
     */
    @Update("UPDATE elderly_subject_type SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 根据类型名称查询（用于唯一性校验）
     * @param typeName 类型名称
     * @return 养老服务主体类型对象
     */
    @Select("SELECT id, type_name as typeName, sort, status, create_time as createTime FROM elderly_subject_type WHERE type_name = #{typeName}")
    ElderlySubjectType selectByTypeName(String typeName);
}