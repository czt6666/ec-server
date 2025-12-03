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
    @Insert("insert into study_tour_base(base_name, operation_unit, address, latitude, longitude, legal_representative, unified_social_credit_code, qualification_cert, feature_desc, business_status, contact_person, contact_phone, create_time, update_time) values(#{baseName}, #{operationUnit}, #{address}, #{latitude}, #{longitude}, #{legalRepresentative}, #{unifiedSocialCreditCode}, #{qualificationCert}, #{featureDesc}, #{businessStatus}, #{contactPerson}, #{contactPhone}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(StudyTourBase studyTourBase);

    /**
     * 根据ID查询研学基地
     */
    @Select("select * from study_tour_base where id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "baseName", column = "base_name"),
        @Result(property = "operationUnit", column = "operation_unit"),
        @Result(property = "address", column = "address"),
        @Result(property = "latitude", column = "latitude"),
        @Result(property = "longitude", column = "longitude"),
        @Result(property = "legalRepresentative", column = "legal_representative"),
        @Result(property = "unifiedSocialCreditCode", column = "unified_social_credit_code"),
        @Result(property = "qualificationCert", column = "qualification_cert"),
        @Result(property = "featureDesc", column = "feature_desc"),
        @Result(property = "businessStatus", column = "business_status"),
        @Result(property = "contactPerson", column = "contact_person"),
        @Result(property = "contactPhone", column = "contact_phone"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    StudyTourBase selectById(Long id);

    /**
     * 查询研学基地列表
     */
    @Select("<script>" +
            "select * from study_tour_base " +
            "<where>" +
            "<if test='baseName != null and baseName != \"\"'>" +
            "and base_name like concat('%', #{baseName}, '%')" +
            "</if>" +
            "<if test='operationUnit != null and operationUnit != \"\"'>" +
            "and operation_unit like concat('%', #{operationUnit}, '%')" +
            "</if>" +
            "<if test='businessStatus != null'>" +
            "and business_status = #{businessStatus}" +
            "</if>" +
            "</where>" +
            "order by create_time desc " +
            "limit #{offset}, #{pageSize}" +
            "</script>")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "baseName", column = "base_name"),
        @Result(property = "operationUnit", column = "operation_unit"),
        @Result(property = "address", column = "address"),
        @Result(property = "latitude", column = "latitude"),
        @Result(property = "longitude", column = "longitude"),
        @Result(property = "legalRepresentative", column = "legal_representative"),
        @Result(property = "unifiedSocialCreditCode", column = "unified_social_credit_code"),
        @Result(property = "qualificationCert", column = "qualification_cert"),
        @Result(property = "featureDesc", column = "feature_desc"),
        @Result(property = "businessStatus", column = "business_status"),
        @Result(property = "contactPerson", column = "contact_person"),
        @Result(property = "contactPhone", column = "contact_phone"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    List<StudyTourBase> selectList(@Param("baseName") String baseName, @Param("operationUnit") String operationUnit, @Param("businessStatus") Integer businessStatus, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 查询研学基地数量
     */
    @Select("<script>" +
            "select count(*) from study_tour_base " +
            "<where>" +
            "<if test='baseName != null and baseName != \"\"'>" +
            "and base_name like concat('%', #{baseName}, '%')" +
            "</if>" +
            "<if test='operationUnit != null and operationUnit != \"\"'>" +
            "and operation_unit like concat('%', #{operationUnit}, '%')" +
            "</if>" +
            "<if test='businessStatus != null'>" +
            "and business_status = #{businessStatus}" +
            "</if>" +
            "</where>" +
            "</script>")
    int count(@Param("baseName") String baseName, @Param("operationUnit") String operationUnit, @Param("businessStatus") Integer businessStatus);

    /**
     * 更新研学基地
     */
    @Update("update study_tour_base set base_name = #{baseName}, operation_unit = #{operationUnit}, address = #{address}, latitude = #{latitude}, longitude = #{longitude}, legal_representative = #{legalRepresentative}, unified_social_credit_code = #{unifiedSocialCreditCode}, qualification_cert = #{qualificationCert}, feature_desc = #{featureDesc}, business_status = #{businessStatus}, contact_person = #{contactPerson}, contact_phone = #{contactPhone}, update_time = #{updateTime} where id = #{id}")
    void update(StudyTourBase studyTourBase);

    /**
     * 根据ID删除研学基地
     */
    @Delete("delete from study_tour_base where id = #{id}")
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
    @Select("select * from study_tour_base order by create_time desc")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "baseName", column = "base_name"),
        @Result(property = "operationUnit", column = "operation_unit"),
        @Result(property = "address", column = "address"),
        @Result(property = "latitude", column = "latitude"),
        @Result(property = "longitude", column = "longitude"),
        @Result(property = "legalRepresentative", column = "legal_representative"),
        @Result(property = "unifiedSocialCreditCode", column = "unified_social_credit_code"),
        @Result(property = "qualificationCert", column = "qualification_cert"),
        @Result(property = "featureDesc", column = "feature_desc"),
        @Result(property = "businessStatus", column = "business_status"),
        @Result(property = "contactPerson", column = "contact_person"),
        @Result(property = "contactPhone", column = "contact_phone"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    List<StudyTourBase> selectAll();
}