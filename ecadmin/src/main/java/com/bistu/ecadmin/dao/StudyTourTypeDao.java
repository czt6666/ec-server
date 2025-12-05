package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.StudyTourType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudyTourTypeDao {

    /**
     * 插入研学类型
     */
    @Insert("insert into study_tour_type(type_name, sort, status, create_time) VALUES (#{typeName}, #{sort}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(StudyTourType studyTourType);

    /**
     * 根据ID查询研学类型
     */
    @Select("select * from study_tour_type where id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "typeName", column = "type_name"),
        @Result(property = "sort", column = "sort"),
        @Result(property = "status", column = "status"),
        @Result(property = "createTime", column = "create_time")
    })
    StudyTourType selectById(Long id);

    /**
     * 查询研学类型列表
     */
    @Select("<script>" +
            "select * from study_tour_type " +
            "<where>" +
            "<if test='typeName != null and typeName != \"\"'>" +
            "and type_name like concat('%', #{typeName}, '%')" +
            "</if>" +
            "<if test='status != null'>" +
            "and status = #{status}" +
            "</if>" +
            "</where>" +
            "order by sort asc, create_time desc " +
            "limit #{offset}, #{pageSize}" +
            "</script>")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "typeName", column = "type_name"),
        @Result(property = "sort", column = "sort"),
        @Result(property = "status", column = "status"),
        @Result(property = "createTime", column = "create_time")
    })
    List<StudyTourType> selectList(@Param("typeName") String typeName, @Param("status") Integer status, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 查询研学类型数量
     */
    @Select("<script>" +
            "select count(*) from study_tour_type " +
            "<where>" +
            "<if test='typeName != null and typeName != \"\"'>" +
            "and type_name like concat('%', #{typeName}, '%')" +
            "</if>" +
            "<if test='status != null'>" +
            "and status = #{status}" +
            "</if>" +
            "</where>" +
            "</script>")
    int count(@Param("typeName") String typeName, @Param("status") Integer status);

    /**
     * 更新研学类型
     */
    @Update("update study_tour_type set type_name = #{typeName}, sort = #{sort}, status = #{status} where id = #{id}")
    void update(StudyTourType studyTourType);

    /**
     * 根据ID删除研学类型
     */
    @Delete("delete from study_tour_type where id = #{id}")
    void deleteById(Long id);

    /**
     * 更新研学类型状态
     */
    @Update("update study_tour_type set status = #{status} where id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据类型名称查询研学类型
     */
    @Select("select * from study_tour_type where type_name = #{typeName}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "typeName", column = "type_name"),
        @Result(property = "sort", column = "sort"),
        @Result(property = "status", column = "status"),
        @Result(property = "createTime", column = "create_time")
    })
    StudyTourType selectByTypeName(String typeName);

    /**
     * 查询所有启用的研学类型
     */
    @Select("select * from study_tour_type where status = 1 order by sort asc, create_time desc")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "typeName", column = "type_name"),
        @Result(property = "sort", column = "sort"),
        @Result(property = "status", column = "status"),
        @Result(property = "createTime", column = "create_time")
    })
    List<StudyTourType> selectAllEnabledTypes();
}