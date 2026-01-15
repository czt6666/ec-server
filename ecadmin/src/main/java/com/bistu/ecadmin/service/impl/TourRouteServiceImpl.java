package com.bistu.ecadmin.service.impl;

import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.TokenUtil;
import com.bistu.ecadmin.dao.TourRouteMapper;
import com.bistu.ecadmin.dao.TourCompanyMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.TourRoute;
import com.bistu.ecadmin.pojo.TourCompany;
import com.bistu.ecadmin.service.TourRouteService;
import com.bistu.ecadmin.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TourRouteServiceImpl implements TourRouteService {

    @Autowired
    private TourRouteMapper tourRouteMapper;
    
    @Autowired
    private TourCompanyMapper tourCompanyMapper;
    
    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public Result<?> create(TourRoute route) {
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

            // 验证公司是否存在，并检查权限
            if (route.getCompanyId() != null) {
                TourCompany company = tourCompanyMapper.selectById(route.getCompanyId());
                if (company == null) {
                    return Result.error("关联的公司不存在");
                }
                
                if (!isAdmin) {
                    // 商家用户：只能在自己公司下创建线路
                    if (company.getUserId() == null || !company.getUserId().equals((long) userInfo.getUserId())) {
                        throw new IllegalArgumentException("无权在其他公司下创建线路");
                    }
                }
            } else {
                throw new IllegalArgumentException("必须关联一个公司");
            }
        } else {
            throw new IllegalArgumentException("未登录，无法新增旅游线路");
        }

        if (route.getName() == null || route.getName().trim().isEmpty()) {
            return Result.error("路线名称不能为空");
        }
        int dup = tourRouteMapper.countByName(route.getName(), null);
        if (dup > 0) {
            return Result.error("路线名称已存在");
        }
        if (route.getBizStatus() == null) {
            route.setBizStatus(1);
        }
        if (route.getStatus() == null) {
            route.setStatus(1);
        }
        int inserted = tourRouteMapper.insert(route);
        return inserted > 0 ? Result.success("创建成功") : Result.error("创建失败");
    }

    @Override
    public Result<?> update(TourRoute route) {
        // 权限校验：商家只能修改自己公司下的线路
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法更新旅游线路");
        }

        // update 方法只需要检查记录是否存在，不需要 isCollect 信息，传入 null
        TourRoute old = tourRouteMapper.selectById(route.getId(), null);
        if (old == null) {
            return Result.error("路线不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能修改自己公司下的线路
            if (old.getCompanyId() != null) {
                TourCompany company = tourCompanyMapper.selectById(old.getCompanyId());
                if (company == null || company.getUserId() == null || !company.getUserId().equals((long) userInfo.getUserId())) {
                    throw new IllegalArgumentException("无权修改其他公司的线路");
                }
            } else {
                throw new IllegalArgumentException("线路未关联公司，无法验证权限");
            }
            
            // 商家用户不能修改公司关联（如果尝试修改）
            if (route.getCompanyId() != null && !route.getCompanyId().equals(old.getCompanyId())) {
                throw new IllegalArgumentException("无权修改线路所属公司");
            }

            // 商家用户不能修改经营状态（上架/下架由管理员控制）
            route.setBizStatus(old.getBizStatus());
        }

        if (route.getName() != null && !route.getName().equals(old.getName())) {
            int dup = tourRouteMapper.countByName(route.getName(), route.getId());
            if (dup > 0) {
                return Result.error("路线名称已存在");
            }
        }
        // 验证公司是否存在（如果提供了companyId）
        if (route.getCompanyId() != null) {
            TourCompany company = tourCompanyMapper.selectById(route.getCompanyId());
            if (company == null) {
                return Result.error("关联的公司不存在");
            }
        }
        int updated = tourRouteMapper.update(route);
        return updated > 0 ? Result.success("更新成功") : Result.error("更新失败");
    }

    @Override
    public boolean publish(Long id) {
        // 只有管理员可以上架
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法上架旅游线路");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以上架旅游线路");
        }

        TourRoute route = new TourRoute();
        route.setId(id);
        route.setBizStatus(1); // 1-发布
        return tourRouteMapper.update(route) > 0;
    }

    @Override
    public boolean unpublish(Long id) {
        // 只有管理员可以下架
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法下架旅游线路");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以下架旅游线路");
        }

        TourRoute route = new TourRoute();
        route.setId(id);
        route.setBizStatus(3); // 3-待审核/暂停
        return tourRouteMapper.update(route) > 0;
    }

    @Override
    public Result<?> delete(Long id) {
        // 权限校验：商家只能删除自己公司下的线路
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法删除旅游线路");
        }

        // delete 方法只需要检查记录是否存在，不需要 isCollect 信息，传入 null
        TourRoute old = tourRouteMapper.selectById(id, null);
        if (old == null) {
            return Result.error("路线不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能删除自己公司下的线路
            if (old.getCompanyId() != null) {
                TourCompany company = tourCompanyMapper.selectById(old.getCompanyId());
                if (company == null || company.getUserId() == null || !company.getUserId().equals((long) userInfo.getUserId())) {
                    throw new IllegalArgumentException("无权删除其他公司的线路");
                }
            } else {
                throw new IllegalArgumentException("线路未关联公司，无法验证权限");
            }
        }

        int deleted = tourRouteMapper.delete(id);
        return deleted > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    @Override
    public Result<PageResult<TourRoute>> list(Integer page, Integer limit, String name, Integer bizStatus, Long companyId) {
        try {
            // 根据当前登录用户限制可见线路：
            // - 管理员(userId=10011 或 roleId 包含 1)：查看全部线路
            // - 普通商家用户：仅查看自己公司下的线路
            
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
            
            SessionUserInfo userInfo = null;
            Long merchantUserId = null;
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
                    // 商家用户只能查看自己公司下的线路
                    merchantUserId = (long) userInfo.getUserId();
                }
            }
        
        int p = (page == null || page < 1) ? 1 : page;
        int l = (limit == null || limit < 1) ? 10 : limit;
        int offset = (p - 1) * l;
            
            log.debug("查询旅游线路列表 - page: {}, limit: {}, name: {}, bizStatus: {}, companyId: {}, merchantUserId: {}", 
                    p, l, name, bizStatus, companyId, merchantUserId);
            
            List<TourRoute> list = tourRouteMapper.page(name, bizStatus, companyId, merchantUserId, userId, offset, l);
            int total = tourRouteMapper.count(name, bizStatus, companyId, merchantUserId);
            
        PageResult<TourRoute> pr = new PageResult<>();
        pr.setTotal(total);
        pr.setRecords(list);
        return Result.success(pr);
        } catch (Exception e) {
            log.error("查询旅游线路列表失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    @Override
    public TourRoute getById(Long id) {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        return tourRouteMapper.selectById(id, userId);
    }
}

