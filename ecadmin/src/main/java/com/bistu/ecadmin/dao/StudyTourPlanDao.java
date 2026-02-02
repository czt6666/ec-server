package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.StudyTourPlan;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface StudyTourPlanDao {

    /**
     * 插入研学方案
     */
    @Insert("INSERT INTO study_tour_plan(plan_name, base_id, route, brief_intro, details, suitable_crowd, duration, status) " +
            "VALUES(#{planName}, #{baseId}, #{route}, #{briefIntro}, #{details}, #{suitableCrowd}, #{duration}, #{status})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(StudyTourPlan studyTourPlan);

    /**
     * 根据id查询研学方案
     */
    @Select("SELECT id, plan_name AS planName, base_id AS baseId, route, brief_intro AS briefIntro, details, suitable_crowd AS suitableCrowd, duration, status, create_time AS createTime, update_time AS updateTime, " +
            "COALESCE((SELECT COUNT(*) FROM user_collect uc WHERE uc.target_type = 'study_plan' AND uc.target_id = CAST(study_tour_plan.id AS CHAR)), 0) AS collectNumber, " +
            "CASE WHEN #{userId} IS NOT NULL AND EXISTS (SELECT 1 FROM user_collect uc2 WHERE uc2.user_id = #{userId} AND uc2.target_type = 'study_plan' AND uc2.target_id = CAST(study_tour_plan.id AS CHAR)) THEN 1 ELSE 0 END AS isCollect " +
            "FROM study_tour_plan WHERE id = #{id}"
    )
    StudyTourPlan getById(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 更新研学方案
     */
    void update(StudyTourPlan studyTourPlan);

    /**
     * 根据id删除研学方案
     */
    @Delete("DELETE FROM study_tour_plan WHERE id = #{id}")
    void deleteById(Long id);

    /**
     * 分页查询研学方案
     */
    List<StudyTourPlan> list(@Param("planName") String planName, @Param("baseId") Long baseId, @Param("status") Integer status, @Param("merchantUserId") Long merchantUserId, @Param("userId") Long userId);

    /**
     * 查询所有启用的研学方案
     */
    List<StudyTourPlan> listAllEnabled(@Param("merchantUserId") Long merchantUserId);

    /**
     * 插入方案图片
     */
    void insertImage(Map<String, Object> imageData);

    /**
     * 根据方案ID获取图片列表
     */
    List<Map<String, Object>> getImagesByPlanId(Long planId);

    /**
     * 删除方案图片
     */
    void deleteImage(Long imageId);

    /**
     * 删除方案关联的所有图片
     */
    void deleteImagesByPlanId(Long planId);

    /**
     * 设置封面图
     */
    void setCoverImage(Long imageId);

    /**
     * 更新图片排序
     */
    void updateImageSort(Map<String, Object> sortData);
}
