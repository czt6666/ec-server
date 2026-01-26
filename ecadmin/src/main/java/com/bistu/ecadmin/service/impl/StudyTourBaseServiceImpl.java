package com.bistu.ecadmin.service.impl;

import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.TokenUtil;
import com.bistu.ecadmin.dao.StudyTourBaseDao;
import com.bistu.ecadmin.dao.StudyTourTypeDao;
import com.bistu.ecadmin.pojo.StudyTourBase;
import com.bistu.ecadmin.pojo.StudyTourType;
import com.bistu.ecadmin.pojo.StudyTourBaseTypeRel;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.StudyTourBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StudyTourBaseServiceImpl implements StudyTourBaseService {

    @Autowired
    private StudyTourBaseDao studyTourBaseDao;

    @Autowired
    private StudyTourTypeDao studyTourTypeDao;

    @Autowired
    private TokenUtil tokenUtil;

    /**
     * 创建研学基地
     */
    @Override
    @Transactional
    public Result createStudyTourBase(StudyTourBase studyTourBase) {
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

            if (!isAdmin) {
                // 商家用户：自动设置userId，营业状态强制设为暂停（2），表示待审核
                studyTourBase.setUserId((long) userInfo.getUserId());
                studyTourBase.setBusinessStatus(2); // 2-暂停（待审核），商家用户新增时强制为待审核状态
            } else {
                // 管理员：如果没有设置businessStatus，默认为1（营业中）
                if (studyTourBase.getBusinessStatus() == null) {
                    studyTourBase.setBusinessStatus(1); // 1-营业中
                }
            }
        } else {
            throw new IllegalArgumentException("未登录，无法新增研学基地");
        }

        // 统一社会信用代码唯一性校验
        if (studyTourBase.getUnifiedSocialCreditCode() != null && !studyTourBase.getUnifiedSocialCreditCode().trim().isEmpty()) {
            int creditCodeDup = studyTourBaseDao.countByUnifiedSocialCreditCode(studyTourBase.getUnifiedSocialCreditCode(), null);
            if (creditCodeDup > 0) {
                return Result.error("统一社会信用代码已存在");
            }
        }

        // 设置创建时间和更新时间
        studyTourBase.setCreateTime(LocalDateTime.now());
        studyTourBase.setUpdateTime(LocalDateTime.now());

        // 插入数据
        studyTourBaseDao.insert(studyTourBase);
        
        return Result.success(studyTourBase.getId());
    }

    /**
     * 查询研学基地列表
     */
    @Override
    public Result<PageResult<StudyTourBase>> listStudyTourBases(String baseName, String operationUnit, Integer businessStatus, int page, int pageSize) {
        // 根据当前登录用户限制可见基地：
        // - 管理员(userId=10011 或 roleId 包含 1)：查看全部基地
        // - 普通商家用户：仅查看自己(userId)名下的基地
        
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            // 没有管理后台 token：可能是小程序端或匿名访问
            log.debug("未获取到管理后台token（可能是小程序端或匿名访问）: {}", e.getMessage());
        }

        Long merchantUserId = null;
        // 如果从 token 获取到用户信息，进行权限过滤
        if (userInfo != null) {
            List<Integer> roleIds = userInfo.getRoleIds();
            boolean isAdmin = (userInfo.getUserId() == 10011)
                    || (roleIds != null && roleIds.contains(1));
            if (!isAdmin) {
                // 商家用户只能查看自己的数据
                merchantUserId = (long) userInfo.getUserId();
            }
        } else {
            // 小程序端 / 匿名访问：如果未显式传入 businessStatus，则只展示营业中的基地（business_status = 1）
            if (businessStatus == null) {
                businessStatus = 1;
            }
        }

        // 计算总记录数
        int total = studyTourBaseDao.count(baseName, operationUnit, businessStatus, merchantUserId);

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 分页查询
        List<StudyTourBase> list = studyTourBaseDao.selectList(baseName, operationUnit, businessStatus, merchantUserId, offset, pageSize);

        // 封装分页结果
        PageResult<StudyTourBase> pageResult = new PageResult<>(total, list);
        return Result.success(pageResult);
    }

    /**
     * 更新研学基地
     */
    @Override
    public Result updateStudyTourBase(StudyTourBase studyTourBase) {
        // 权限校验：商家只能修改自己的数据
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法更新研学基地");
        }

        // 检查要更新的基地是否存在
        StudyTourBase existing = studyTourBaseDao.selectById(studyTourBase.getId());
        if (existing == null) {
            return Result.error("研学基地不存在");
        }

        // 统一社会信用代码唯一性校验（排除自身）
        if (studyTourBase.getUnifiedSocialCreditCode() != null && !studyTourBase.getUnifiedSocialCreditCode().trim().isEmpty()) {
            if (existing.getUnifiedSocialCreditCode() == null || !studyTourBase.getUnifiedSocialCreditCode().equals(existing.getUnifiedSocialCreditCode())) {
                int creditCodeDup = studyTourBaseDao.countByUnifiedSocialCreditCode(studyTourBase.getUnifiedSocialCreditCode(), studyTourBase.getId());
                if (creditCodeDup > 0) {
                    return Result.error("统一社会信用代码已存在");
                }
            }
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能修改自己的数据，且不能修改businessStatus（不能上架下架）
            if (existing.getUserId() == null || !existing.getUserId().equals((long) userInfo.getUserId())) {
                throw new IllegalArgumentException("无权修改其他商家的研学基地");
            }
            // 商家不能修改businessStatus，保持原状态
            studyTourBase.setBusinessStatus(existing.getBusinessStatus());
        }

        // 设置更新时间
        studyTourBase.setUpdateTime(LocalDateTime.now());

        // 更新数据
        studyTourBaseDao.update(studyTourBase);
        return Result.success();
    }

    /**
     * 删除研学基地
     */
    @Override
    @Transactional
    public Result deleteStudyTourBase(Long id) {
        // 权限校验：商家只能删除自己的数据
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法删除研学基地");
        }

        // 检查要删除的基地是否存在
        StudyTourBase existing = studyTourBaseDao.selectById(id);
        if (existing == null) {
            return Result.error("研学基地不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能删除自己的数据
            if (existing.getUserId() == null || !existing.getUserId().equals((long) userInfo.getUserId())) {
                throw new IllegalArgumentException("无权删除其他商家的研学基地");
            }
        }

        // 删除基地与类型关联关系
        studyTourBaseDao.deleteBaseTypeRelsByBaseId(id);

        // 删除基地数据
        studyTourBaseDao.deleteById(id);
        return Result.success();
    }

    /**
     * 保存基地与研学类型的关联关系
     */
    @Override
    @Transactional
    public Result saveBaseTypes(Long baseId, List<Long> typeIds) {
        // 检查基地是否存在
        StudyTourBase existing = studyTourBaseDao.selectById(baseId);
        if (existing == null) {
            return Result.error("研学基地不存在");
        }

        // 删除原有的关联关系
        studyTourBaseDao.deleteBaseTypeRelsByBaseId(baseId);

        // 插入新的关联关系
        if (typeIds != null && !typeIds.isEmpty()) {
            for (Long typeId : typeIds) {
                // 检查类型是否存在
                StudyTourType type = studyTourTypeDao.selectById(typeId);
                if (type != null) {
                    StudyTourBaseTypeRel rel = new StudyTourBaseTypeRel();
                    rel.setBaseId(baseId);
                    rel.setTypeId(typeId);
                    rel.setCreateTime(LocalDateTime.now());
                    studyTourBaseDao.insertBaseTypeRel(rel);
                }
            }
        }

        return Result.success();
    }

    /**
     * 获取基地关联的研学类型
     */
    @Override
    public Result<List<StudyTourType>> getAssociatedTypes(Long baseId) {
        // 检查基地是否存在
        StudyTourBase existing = studyTourBaseDao.selectById(baseId);
        if (existing == null) {
            return Result.error("研学基地不存在");
        }

        // 查询关联的类型
        List<StudyTourType> types = studyTourBaseDao.selectTypesByBaseId(baseId);
        return Result.success(types);
    }

    /**
     * 查询所有研学基地列表
     */
    @Override
    public Result<List<StudyTourBase>> listStudyTourBases() {
        // 查询所有基地
        List<StudyTourBase> list = studyTourBaseDao.selectAll();

        // 根据当前登录用户限制可见基地：
        // - 管理员(userId=10011 或 roleId 包含 1)：查看全部基地
        // - 普通商家用户：仅查看自己(userId)名下的基地
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            // 没有管理后台 token：默认返回全部（一般不会走到这里）
            log.debug("未获取到管理后台token，listStudyTourBases 将返回全部基地: {}", e.getMessage());
        }

        if (userInfo == null) {
            return Result.success(list);
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (isAdmin) {
            return Result.success(list);
        }

        Long merchantUserId = (long) userInfo.getUserId();
        List<StudyTourBase> filtered = new java.util.ArrayList<>();
        for (StudyTourBase base : list) {
            if (base.getUserId() != null && base.getUserId().equals(merchantUserId)) {
                filtered.add(base);
            }
        }
        return Result.success(filtered);
    }

    @Override
    public boolean publish(Long id) {
        // 只有管理员可以上架
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法上架研学基地");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以上架研学基地");
        }

        StudyTourBase base = new StudyTourBase();
        base.setId(id);
        base.setBusinessStatus(1); // 1-营业中（已上架）
        studyTourBaseDao.update(base);
        return true;
    }

    @Override
    public boolean unpublish(Long id) {
        // 只有管理员可以下架
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法下架研学基地");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以下架研学基地");
        }

        StudyTourBase base = new StudyTourBase();
        base.setId(id);
        base.setBusinessStatus(2); // 2-待审核/下架
        studyTourBaseDao.update(base);
        return true;
    }
}