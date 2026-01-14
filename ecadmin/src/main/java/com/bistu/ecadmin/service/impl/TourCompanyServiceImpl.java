package com.bistu.ecadmin.service.impl;

import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.TokenUtil;
import com.bistu.ecadmin.dao.TourCompanyMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.TourCompany;
import com.bistu.ecadmin.service.TourCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TourCompanyServiceImpl implements TourCompanyService {

    @Autowired
    private TourCompanyMapper tourCompanyMapper;
    
    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public Result<?> create(TourCompany company) {
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
                company.setUserId((long) userInfo.getUserId());
                company.setBusinessStatus(2); // 2-暂停（待审核），商家用户新增时强制为待审核状态
            } else {
                // 管理员：如果没有设置userId，设置为null（允许管理员创建时指定）
                // 如果没有设置businessStatus，默认为1（营业中）
                if (company.getBusinessStatus() == null) {
                    company.setBusinessStatus(1); // 1-营业中
                }
            }
        } else {
            throw new IllegalArgumentException("未登录，无法新增旅游公司");
        }

        if (company.getName() == null || company.getName().trim().isEmpty()) {
            return Result.error("公司名称不能为空");
        }
        int dup = tourCompanyMapper.countByName(company.getName(), null);
        if (dup > 0) {
            return Result.error("公司名称已存在");
        }
        if (company.getStatus() == null) {
            company.setStatus(1);
        }
        int inserted = tourCompanyMapper.insert(company);
        return inserted > 0 ? Result.success("创建成功") : Result.error("创建失败");
    }

    @Override
    public Result<?> update(TourCompany company) {
        // 权限校验：商家只能修改自己的数据
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法更新旅游公司");
        }

        // 查询原数据
        TourCompany old = tourCompanyMapper.selectById(company.getId());
        if (old == null) {
            return Result.error("公司不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能修改自己的数据，且不能修改businessStatus（不能上架下架）
            if (old.getUserId() == null || !old.getUserId().equals((long) userInfo.getUserId())) {
                throw new IllegalArgumentException("无权修改其他商家的旅游公司");
            }
            // 商家不能修改businessStatus，保持原状态
            company.setBusinessStatus(old.getBusinessStatus());
        }

        if (company.getName() != null && !company.getName().equals(old.getName())) {
            int dup = tourCompanyMapper.countByName(company.getName(), company.getId());
            if (dup > 0) {
                return Result.error("公司名称已存在");
            }
        }
        int updated = tourCompanyMapper.update(company);
        return updated > 0 ? Result.success("更新成功") : Result.error("更新失败");
    }

    @Override
    public Result<?> delete(Long id) {
        // 权限校验：商家只能删除自己的数据
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法删除旅游公司");
        }

        // 查询原数据
        TourCompany old = tourCompanyMapper.selectById(id);
        if (old == null) {
            return Result.error("公司不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能删除自己的数据
            if (old.getUserId() == null || !old.getUserId().equals((long) userInfo.getUserId())) {
                throw new IllegalArgumentException("无权删除其他商家的旅游公司");
            }
        }

        int deleted = tourCompanyMapper.delete(id);
        return deleted > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    @Override
    public Result<PageResult<TourCompany>> list(Integer page, Integer limit, String name, Integer status) {
        // 根据当前登录用户限制可见公司：
        // - 管理员(userId=10011 或 roleId 包含 1)：查看全部公司
        // - 普通商家用户：仅查看自己(userId)名下的公司
        
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
            // 小程序端 / 匿名访问：如果未显式传入 status，则只展示营业中的公司（business_status = 1）
            if (status == null) {
                status = 1;
            }
        }

        int p = (page == null || page < 1) ? 1 : page;
        int l = (limit == null || limit < 1) ? 10 : limit;
        int offset = (p - 1) * l;
        List<TourCompany> list = tourCompanyMapper.page(name, status, merchantUserId, offset, l);
        int total = tourCompanyMapper.count(name, status, merchantUserId);
        PageResult<TourCompany> pr = new PageResult<>();
        pr.setTotal(total);
        pr.setRecords(list);
        return Result.success(pr);
    }

    @Override
    public TourCompany getById(Long id) {
        return tourCompanyMapper.selectById(id);
    }

    @Override
    public boolean publish(Long id) {
        // 只有管理员可以上架
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法上架旅游公司");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以上架旅游公司");
        }

        TourCompany company = new TourCompany();
        company.setId(id);
        company.setBusinessStatus(1); // 1-营业中（已上架）
        return tourCompanyMapper.update(company) > 0;
    }

    @Override
    public boolean unpublish(Long id) {
        // 只有管理员可以下架
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法下架旅游公司");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以下架旅游公司");
        }

        TourCompany company = new TourCompany();
        company.setId(id);
        company.setBusinessStatus(3); // 3-已注销（已下架）
        return tourCompanyMapper.update(company) > 0;
    }
}

