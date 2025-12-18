package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.StudyTourPlan;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.dao.StudyTourPlanDao;
import com.bistu.ecadmin.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class StudyTourPlanService {
    
    @Autowired
    private StudyTourPlanDao studyTourPlanDao;
    
    /**
     * 新增研学方案
     */
    public Result createStudyTourPlan(StudyTourPlan studyTourPlan) {
        try {
            studyTourPlanDao.insert(studyTourPlan);
            return Result.success("新增成功");
        } catch (Exception e) {
            log.error("新增研学方案失败", e);
            return Result.error("新增失败");
        }
    }
    
    /**
     * 研学方案分页查询
     */
    public Result<PageResult<StudyTourPlan>> listStudyTourPlans(String planName, Long baseId, Integer status, int page, int pageSize, Long userId) {
        try {
            // 如果参数中没有userId，则从UserContext获取
            if (userId == null) {
                userId = UserContext.getUserId();
            }
            
            PageHelper.startPage(page, pageSize);
            List<StudyTourPlan> studyTourPlanList = studyTourPlanDao.list(planName, baseId, status, userId);
            PageInfo<StudyTourPlan> pageInfo = new PageInfo<>(studyTourPlanList);
            PageResult<StudyTourPlan> pageResult = new PageResult<>();
            pageResult.setRecords(pageInfo.getList());
            pageResult.setTotal(pageInfo.getTotal());
            return Result.success(pageResult);
        } catch (Exception e) {
            log.error("查询研学方案失败", e);
            return Result.error("查询失败");
        }
    }
    
    /**
     * 修改研学方案
     */
    public Result updateStudyTourPlan(StudyTourPlan studyTourPlan) {
        try {
            StudyTourPlan existing = studyTourPlanDao.getById(studyTourPlan.getId(), null);
            if (existing == null) {
                return Result.error("研学方案不存在");
            }
            studyTourPlanDao.update(studyTourPlan);
            return Result.success("修改成功");
        } catch (Exception e) {
            log.error("修改研学方案失败", e);
            return Result.error("修改失败");
        }
    }
    
    /**
     * 删除研学方案
     */
    public Result deleteStudyTourPlan(Long id) {
        try {
            StudyTourPlan existing = studyTourPlanDao.getById(id, null);
            if (existing == null) {
                return Result.error("研学方案不存在");
            }
            studyTourPlanDao.deleteById(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除研学方案失败", e);
            return Result.error("删除失败");
        }
    }
    
    /**
     * 查询所有启用的研学方案（用于下拉选择）
     */
    public Result<List<StudyTourPlan>> listAllEnabled() {
        try {
            List<StudyTourPlan> studyTourPlanList = studyTourPlanDao.listAllEnabled();
            return Result.success(studyTourPlanList);
        } catch (Exception e) {
            log.error("查询启用的研学方案失败", e);
            return Result.error("查询失败");
        }
    }
}