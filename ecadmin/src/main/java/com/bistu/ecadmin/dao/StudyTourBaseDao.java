package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.StudyTourBase;
import com.bistu.ecadmin.pojo.StudyTourType;
import com.bistu.ecadmin.pojo.StudyTourBaseTypeRel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudyTourBaseDao {

    /**
     * 插入研学基地
     */
    void insert(StudyTourBase studyTourBase);

    /**
     * 根据ID查询研学基地
     */
    StudyTourBase selectById(Long id);

    /**
     * 查询研学基地列表
     */
    List<StudyTourBase> selectList(@Param("baseName") String baseName, @Param("operationUnit") String operationUnit, @Param("businessStatus") Integer businessStatus, @Param("merchantUserId") Long merchantUserId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 查询研学基地数量
     */
    int count(@Param("baseName") String baseName, @Param("operationUnit") String operationUnit, @Param("businessStatus") Integer businessStatus, @Param("merchantUserId") Long merchantUserId);

    /**
     * 更新研学基地
     */
    void update(StudyTourBase studyTourBase);

    /**
     * 根据ID删除研学基地
     */
    void deleteById(Long id);

    /**
     * 插入基地与类型关联关系
     */
    @Insert("insert into study_tour_base_type_rel(base_id, type_id, create_time) VALUES (#{baseId}, #{typeId}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertBaseTypeRel(StudyTourBaseTypeRel rel);

    /**
     * 删除基地的所有类型关联关系
     */
    @Delete("delete from study_tour_base_type_rel where base_id = #{baseId}")
    void deleteBaseTypeRelsByBaseId(Long baseId);

    /**
     * 根据基地ID查询关联的类型ID列表
     */
    @Select("select type_id from study_tour_base_type_rel where base_id = #{baseId}")
    List<Long> selectTypeIdsByBaseId(Long baseId);

    /**
     * 根据基地ID查询关联的类型信息
     */
    @Select("select t.* from study_tour_type t inner join study_tour_base_type_rel r on t.id = r.type_id where r.base_id = #{baseId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "typeName", column = "type_name"),
        @Result(property = "sort", column = "sort"),
        @Result(property = "status", column = "status"),
        @Result(property = "createTime", column = "create_time")
    })
    List<StudyTourType> selectTypesByBaseId(Long baseId);

    /**
     * 查询所有研学基地列表
     */
    List<StudyTourBase> selectAll();
}