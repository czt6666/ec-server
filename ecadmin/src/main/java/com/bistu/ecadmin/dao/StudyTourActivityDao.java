package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.StudyTourActivity;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface StudyTourActivityDao {

    /**
     * 插入研学活动
     */
    @Insert("INSERT INTO study_tour_activity(activity_name, plan_id, apply_start_date, apply_end_date, activity_start_date, activity_end_date, price, recruit_num, registered_num, status, remark) " +
            "VALUES(#{activityName}, #{tourPlanId}, #{applyStartDate}, #{applyEndDate}, #{activityStartDate}, #{activityEndDate}, #{price}, #{recruitNum}, #{registeredNum}, #{status}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(StudyTourActivity studyTourActivity);

    /**
     * 根据id查询研学活动
     */
    @Select("SELECT id, activity_name AS activityName, plan_id AS tourPlanId, apply_start_date AS applyStartDate, apply_end_date AS applyEndDate, activity_start_date AS activityStartDate, activity_end_date AS activityEndDate, price, recruit_num AS recruitNum, registered_num AS registeredNum, status, remark, create_time AS createTime, update_time AS updateTime, " +
            "COALESCE((SELECT COUNT(*) FROM user_collect uc WHERE uc.target_type = 'study_activity' AND uc.target_id = CAST(study_tour_activity.id AS CHAR)), 0) AS collectNumber, " +
            "CASE WHEN #{userId} IS NOT NULL AND EXISTS (SELECT 1 FROM user_collect uc2 WHERE uc2.user_id = #{userId} AND uc2.target_type = 'study_activity' AND uc2.target_id = CAST(study_tour_activity.id AS CHAR)) THEN 1 ELSE 0 END AS isCollect " +
            "FROM study_tour_activity WHERE id = #{id}")
    StudyTourActivity getById(@Param("id") Long id, @Param("userId") Long userId);



    /**
     * 根据id删除研学活动
     */
    @Delete("DELETE FROM study_tour_activity WHERE id = #{id}")
    void deleteById(Long id);

    /**
     * 更新研学活动
     */
    void update(StudyTourActivity studyTourActivity);

    /**
     * 分页查询研学活动
     */
    List<StudyTourActivity> list(@Param("activityName") String activityName,
                                 @Param("tourPlanId") Long tourPlanId,
                                 @Param("status") Integer status,
                                 @Param("excludeCancelled") Boolean excludeCancelled,
                                 @Param("merchantUserId") Long merchantUserId,
                                 @Param("userId") Long userId);



    /**
     * 更新已报名人数
     */
    @Update("UPDATE study_tour_activity SET registered_num = #{registeredNum} WHERE id = #{id}")
    void updateRegisteredNum(@Param("id") Long id, @Param("registeredNum") Integer registeredNum);
}
