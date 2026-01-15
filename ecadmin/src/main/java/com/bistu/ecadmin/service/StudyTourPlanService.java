package com.bistu.ecadmin.service;

import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.TokenUtil;
import com.bistu.ecadmin.dao.StudyTourPlanDao;
import com.bistu.ecadmin.dao.StudyTourBaseDao;
import com.bistu.ecadmin.pojo.StudyTourPlan;
import com.bistu.ecadmin.pojo.StudyTourBase;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
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

    @Autowired
    private StudyTourBaseDao studyTourBaseDao;

    @Autowired
    private TokenUtil tokenUtil;

    /**
     * 新增研学方案
     */
    public Result createStudyTourPlan(StudyTourPlan studyTourPlan) {
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

            // 验证基地是否存在，并检查权限
            if (studyTourPlan.getBaseId() != null) {
                StudyTourBase base = studyTourBaseDao.selectById(studyTourPlan.getBaseId());
                if (base == null) {
                    return Result.error("关联的研学基地不存在");
                }
                
                if (!isAdmin) {
                    // 商家用户：只能在自己基地下创建方案
                    if (base.getUserId() == null || !base.getUserId().equals((long) userInfo.getUserId())) {
                        throw new IllegalArgumentException("无权在其他基地下创建研学方案");
                    }
                    // 普通商户创建的方案默认处于禁用/下架状态
                    studyTourPlan.setStatus(0);
                } else {
                    // 管理员未填写状态时，默认启用
                    if (studyTourPlan.getStatus() == null) {
                        studyTourPlan.setStatus(1);
                    }
                }
            } else {
                throw new IllegalArgumentException("必须关联一个研学基地");
            }
        } else {
            throw new IllegalArgumentException("未登录，无法新增研学方案");
        }

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
    public Result<PageResult<StudyTourPlan>> listStudyTourPlans(String planName, Long baseId, Integer status, int page, int pageSize) {
        try {
            // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
            Long userId = UserContext.getUserId();
            
            // 根据当前登录用户限制可见方案：
            // - 管理员(userId=10011 或 roleId 包含 1)：查看全部方案
            // - 普通商家用户：仅查看自己基地下的方案
            
            SessionUserInfo userInfo = null;
            Long merchantUserId = null;
            try {
                userInfo = tokenUtil.getUserInfo();
            } catch (Exception e) {
                log.debug("未获取到管理后台token（可能是小程序端或匿名访问）: {}", e.getMessage());
            }

            // 如果从token获取到用户信息，进行权限过滤（后台管理端）
            if (userInfo != null) {
                List<Integer> roleIds = userInfo.getRoleIds();
                boolean isAdmin = (userInfo.getUserId() == 10011)
                        || (roleIds != null && roleIds.contains(1));
                if (!isAdmin) {
                    // 商家用户只能查看自己基地下的方案
                    merchantUserId = (long) userInfo.getUserId();
                }
            } else {
                // 小程序端 / 匿名访问：如果未显式传入 status，则只展示启用的方案（status = 1）
                if (status == null) {
                    status = 1;
                }
            }
            
            PageHelper.startPage(page, pageSize);
            List<StudyTourPlan> studyTourPlanList = studyTourPlanDao.list(planName, baseId, status, merchantUserId, userId);
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
     * 根据id查询研学方案
     */
    public StudyTourPlan getById(Long id, Long userId) {
        try {
            return studyTourPlanDao.getById(id, userId);
        } catch (Exception e) {
            log.error("查询研学方案失败", e);
            return null;
        }
    }

    /**
     * 修改研学方案
     */
    public Result updateStudyTourPlan(StudyTourPlan studyTourPlan) {
        // 权限校验：商家只能修改自己基地下的方案
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法更新研学方案");
        }

        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        StudyTourPlan existing = studyTourPlanDao.getById(studyTourPlan.getId(), userId);
        if (existing == null) {
            return Result.error("研学方案不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能修改自己基地下的方案
            if (existing.getBaseId() != null) {
                StudyTourBase base = studyTourBaseDao.selectById(existing.getBaseId());
                if (base == null || base.getUserId() == null || !base.getUserId().equals((long) userInfo.getUserId())) {
                    throw new IllegalArgumentException("无权修改其他基地的研学方案");
                }
            } else {
                throw new IllegalArgumentException("方案未关联基地，无法验证权限");
            }
            
            // 商家用户不能修改基地关联（如果尝试修改）
            if (studyTourPlan.getBaseId() != null && !studyTourPlan.getBaseId().equals(existing.getBaseId())) {
                throw new IllegalArgumentException("无权修改方案所属基地");
            }

            // 商家用户不能修改方案状态（上下架仅管理员可控）
            studyTourPlan.setStatus(existing.getStatus());
        }

        try {
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
        // 权限校验：商家只能删除自己基地下的方案
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法删除研学方案");
        }

        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        StudyTourPlan existing = studyTourPlanDao.getById(id, userId);
        if (existing == null) {
            return Result.error("研学方案不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能删除自己基地下的方案
            if (existing.getBaseId() != null) {
                StudyTourBase base = studyTourBaseDao.selectById(existing.getBaseId());
                if (base == null || base.getUserId() == null || !base.getUserId().equals((long) userInfo.getUserId())) {
                    throw new IllegalArgumentException("无权删除其他基地的研学方案");
                }
            } else {
                throw new IllegalArgumentException("方案未关联基地，无法验证权限");
            }
        }

        try {
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
            // 根据当前登录用户限制可见方案（用于后台下拉选择）：
            // - 管理员(userId=10011 或 roleId 包含 1)：查看所有已启用方案
            // - 普通商家用户：仅查看自己基地下的已启用方案
            Long merchantUserId = null;
            try {
                SessionUserInfo userInfo = tokenUtil.getUserInfo();
                if (userInfo != null) {
                    List<Integer> roleIds = userInfo.getRoleIds();
                    boolean isAdmin = (userInfo.getUserId() == 10011)
                            || (roleIds != null && roleIds.contains(1));
                    if (!isAdmin) {
                        merchantUserId = (long) userInfo.getUserId();
                    }
                }
            } catch (Exception e) {
                // 未获取到管理后台token（可能是小程序端或匿名访问），不做商户过滤
                log.debug("未获取到管理后台token，listAllEnabled 不进行商户过滤: {}", e.getMessage());
            }

            List<StudyTourPlan> studyTourPlanList = studyTourPlanDao.listAllEnabled(merchantUserId);
            return Result.success(studyTourPlanList);
        } catch (Exception e) {
            log.error("查询启用的研学方案失败", e);
            return Result.error("查询失败");
        }
    }

    /**
     * 上架研学方案（仅管理员，将状态改为1-启用）
     */
    public boolean publish(Long id) {
        SessionUserInfo userInfo;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法上架研学方案");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以上架研学方案");
        }

        StudyTourPlan plan = new StudyTourPlan();
        plan.setId(id);
        plan.setStatus(1); // 1-启用（上架）
        studyTourPlanDao.update(plan);
        return true;
    }

    /**
     * 下架研学方案（仅管理员，将状态改为0-禁用）
     */
    public boolean unpublish(Long id) {
        SessionUserInfo userInfo;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法下架研学方案");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以下架研学方案");
        }

        StudyTourPlan plan = new StudyTourPlan();
        plan.setId(id);
        plan.setStatus(0); // 0-禁用（下架）
        studyTourPlanDao.update(plan);
        return true;
    }
}