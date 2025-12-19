package com.bistu.ecadmin.service;

import com.bistu.ecadmin.dao.StudyTourActivityDao;
import com.bistu.ecadmin.pojo.StudyTourActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyTourActivityService {

    @Autowired
    private StudyTourActivityDao studyTourActivityDao;

    /**
     * 分页查询研学活动
     */
    public List<StudyTourActivity> list(String activityName, Long planId, Integer status) {
        return studyTourActivityDao.list(activityName, planId, status);
    }

    /**
     * 保存研学活动
     */
    public void save(StudyTourActivity studyTourActivity) {
        studyTourActivityDao.insert(studyTourActivity);
    }

    /**
     * 根据id查询研学活动
     */
    public StudyTourActivity getById(Long id) {
        return studyTourActivityDao.getById(id);
    }

    /**
     * 更新研学活动
     */
    public void update(StudyTourActivity studyTourActivity) {
        studyTourActivityDao.update(studyTourActivity);
    }

    /**
     * 删除研学活动
     */
    public void deleteById(Long id) {
        studyTourActivityDao.deleteById(id);
    }

    /**
     * 获取所有启用的研学活动
     */
    public List<StudyTourActivity> listAllEnabled() {
        // 这里可以根据实际需求定义启用状态，假设状态为1表示启用
        return studyTourActivityDao.list(null, null, 1);
    }
}
