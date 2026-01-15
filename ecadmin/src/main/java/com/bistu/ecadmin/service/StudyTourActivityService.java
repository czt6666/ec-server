package com.bistu.ecadmin.service;

import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.TokenUtil;
import com.bistu.ecadmin.dao.StudyTourActivityDao;
import com.bistu.ecadmin.dao.StudyTourPlanDao;
import com.bistu.ecadmin.dao.StudyTourBaseDao;
import com.bistu.ecadmin.pojo.StudyTourActivity;
import com.bistu.ecadmin.pojo.StudyTourPlan;
import com.bistu.ecadmin.pojo.StudyTourBase;
import com.bistu.ecadmin.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudyTourActivityService {

    @Autowired
    private StudyTourActivityDao studyTourActivityDao;

    @Autowired
    private StudyTourPlanDao studyTourPlanDao;

    @Autowired
    private StudyTourBaseDao studyTourBaseDao;

    @Autowired
    private TokenUtil tokenUtil;

    /**
     * 分页查询研学活动
     */
    public List<StudyTourActivity> list(String activityName, Long tourPlanId, Integer status) {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        
        // 根据当前登录用户限制可见活动：
        // - 管理员(userId=10011 或 roleId 包含 1)：查看全部活动
        // - 普通商家用户：仅查看自己基地下的活动
        
        SessionUserInfo userInfo = null;
        Long merchantUserId = null;
        Boolean excludeCancelled = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            log.debug("未获取到管理后台token（可能是小程序端或匿名访问）: {}", e.getMessage());
        }

        // 如果从token获取到用户信息，进行权限过滤
        if (userInfo != null) {
            List<Integer> roleIds = userInfo.getRoleIds();
            boolean isAdmin = (userInfo.getUserId() == 10011)
                    || (roleIds != null && roleIds.contains(1));
            if (!isAdmin) {
                // 商家用户只能查看自己基地下的活动
                merchantUserId = (long) userInfo.getUserId();
            }
        } else {
            // 小程序/匿名访问：默认过滤掉“取消(5)”的活动；其余状态都可见
            if (status == null) {
                excludeCancelled = true;
            }
        }
        
        return studyTourActivityDao.list(activityName, tourPlanId, status, excludeCancelled, merchantUserId, userId);
    }

    /**
     * 保存研学活动
     */
    public void save(StudyTourActivity studyTourActivity) {
        // 获取当前登录用户信息
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            log.debug("未获取到管理后台token: {}", e.getMessage());
        }

        if (userInfo != null) {
            // 判断是否为管理员
            List<Integer> roleIds = userInfo.getRoleIds();
            boolean isAdmin = (userInfo.getUserId() == 10011)
                    || (roleIds != null && roleIds.contains(1));

            // 验证方案是否存在，并检查权限
            if (studyTourActivity.getTourPlanId() != null) {
                StudyTourPlan plan = studyTourPlanDao.getById(studyTourActivity.getTourPlanId(), null);
                if (plan == null) {
                    throw new IllegalArgumentException("关联的研学方案不存在");
                }
                
                if (!isAdmin) {
                    // 商家用户：只能在自己基地下的方案中创建活动
                    if (plan.getBaseId() != null) {
                        StudyTourBase base = studyTourBaseDao.selectById(plan.getBaseId());
                        if (base == null || base.getUserId() == null || !base.getUserId().equals((long) userInfo.getUserId())) {
                            throw new IllegalArgumentException("无权在其他基地的方案下创建研学活动");
                        }
                    } else {
                        throw new IllegalArgumentException("方案未关联基地，无法验证权限");
                    }
                }
            } else {
                throw new IllegalArgumentException("必须关联一个研学方案");
            }
        } else {
            throw new IllegalArgumentException("未登录，无法新增研学活动");
        }

        studyTourActivityDao.insert(studyTourActivity);
    }

    /**
     * 根据id查询研学活动
     */
    public StudyTourActivity getById(Long id) {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        StudyTourActivity activity = studyTourActivityDao.getById(id, userId);
        if (activity == null) {
            return null;
        }

        // 小程序/匿名访问：取消(5)的活动不展示（等同下架）
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            // ignore
        }
        if (userInfo == null && activity.getStatus() != null && activity.getStatus() == 5) {
            return null;
        }
        return activity;
    }

    /**
     * 更新研学活动
     */
    public void update(StudyTourActivity studyTourActivity) {
        // 权限校验：商家只能修改自己基地下的活动
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法更新研学活动");
        }

        // 查询原数据
        Long userId = UserContext.getUserId();
        StudyTourActivity existing = studyTourActivityDao.getById(studyTourActivity.getId(), userId);
        if (existing == null) {
            throw new IllegalArgumentException("研学活动不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能修改自己基地下的活动
            if (existing.getTourPlanId() != null) {
                StudyTourPlan plan = studyTourPlanDao.getById(existing.getTourPlanId(), null);
                if (plan == null || plan.getBaseId() == null) {
                    throw new IllegalArgumentException("活动关联的方案或基地不存在，无法验证权限");
                }
                StudyTourBase base = studyTourBaseDao.selectById(plan.getBaseId());
                if (base == null || base.getUserId() == null || !base.getUserId().equals((long) userInfo.getUserId())) {
                    throw new IllegalArgumentException("无权修改其他基地的研学活动");
                }
            } else {
                throw new IllegalArgumentException("活动未关联方案，无法验证权限");
            }
            
            // 商家用户不能修改方案关联（如果尝试修改）
            if (studyTourActivity.getTourPlanId() != null && !studyTourActivity.getTourPlanId().equals(existing.getTourPlanId())) {
                throw new IllegalArgumentException("无权修改活动所属方案");
            }
        }

        studyTourActivityDao.update(studyTourActivity);
    }

    /**
     * 删除研学活动
     */
    public void deleteById(Long id) {
        // 权限校验：商家只能删除自己基地下的活动
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法删除研学活动");
        }

        // 查询原数据
        Long userId = UserContext.getUserId();
        StudyTourActivity existing = studyTourActivityDao.getById(id, userId);
        if (existing == null) {
            throw new IllegalArgumentException("研学活动不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能删除自己基地下的活动
            if (existing.getTourPlanId() != null) {
                StudyTourPlan plan = studyTourPlanDao.getById(existing.getTourPlanId(), null);
                if (plan == null || plan.getBaseId() == null) {
                    throw new IllegalArgumentException("活动关联的方案或基地不存在，无法验证权限");
                }
                StudyTourBase base = studyTourBaseDao.selectById(plan.getBaseId());
                if (base == null || base.getUserId() == null || !base.getUserId().equals((long) userInfo.getUserId())) {
                    throw new IllegalArgumentException("无权删除其他基地的研学活动");
                }
            } else {
                throw new IllegalArgumentException("活动未关联方案，无法验证权限");
            }
        }

        studyTourActivityDao.deleteById(id);
    }

    /**
     * 获取所有启用的研学活动
     */
    public List<StudyTourActivity> listAllEnabled() {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        
        // 小程序端访问，不进行商户过滤
        return studyTourActivityDao.list(null, null, null, true, null, userId);
    }
}
