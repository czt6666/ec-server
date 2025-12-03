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
            "VALUES(#{activityName}, #{planId}, #{applyStartDate}, #{applyEndDate}, #{activityStartDate}, #{activityEndDate}, #{price}, #{recruitNum}, #{registeredNum}, #{status}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(StudyTourActivity studyTourActivity);
    
    /**
     * 根据id查询研学活动
     */
    @Select("SELECT id, activity_name AS activityName, plan_id AS planId, apply_start_date AS applyStartDate, apply_end_date AS applyEndDate, activity_start_date AS activityStartDate, activity_end_date AS activityEndDate, price, recruit_num AS recruitNum, registered_num AS registeredNum, status, remark, create_time AS createTime, update_time AS updateTime FROM study_tour_activity WHERE id = #{id}")
    StudyTourActivity getById(Long id);
    
    /**
     * 更新研学活动
     */
    void update(StudyTourActivity studyTourActivity);
    
    /**
     * 根据id删除研学活动
     */
    @Delete("DELETE FROM study_tour_activity WHERE id = #{id}")
    void deleteById(Long id);
    
    /**
     * 分页查询研学活动
     */
    List<StudyTourActivity> list(@Param("activityName") String activityName, @Param("planId") Long planId, @Param("status") Integer status);
    
    /**
     * 更新已报名人数
     */
    @Update("UPDATE study_tour_activity SET registered_num = #{registeredNum} WHERE id = #{id}")
    void updateRegisteredNum(@Param("id") Long id, @Param("registeredNum") Integer registeredNum);
}