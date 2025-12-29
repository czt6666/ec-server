package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.TourRouteMapper;
import com.bistu.ecadmin.dao.TourCompanyMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.TourRoute;
import com.bistu.ecadmin.pojo.TourCompany;
import com.bistu.ecadmin.service.TourRouteService;
import com.bistu.ecadmin.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourRouteServiceImpl implements TourRouteService {

    @Autowired
    private TourRouteMapper tourRouteMapper;
    
    @Autowired
    private TourCompanyMapper tourCompanyMapper;

    @Override
    public Result<?> create(TourRoute route) {
        if (route.getName() == null || route.getName().trim().isEmpty()) {
            return Result.error("路线名称不能为空");
        }
        int dup = tourRouteMapper.countByName(route.getName(), null);
        if (dup > 0) {
            return Result.error("路线名称已存在");
        }
        // 验证公司是否存在
        if (route.getCompanyId() != null) {
            TourCompany company = tourCompanyMapper.selectById(route.getCompanyId());
            if (company == null) {
                return Result.error("关联的公司不存在");
            }
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
        // update 方法只需要检查记录是否存在，不需要 isCollect 信息，传入 null
        TourRoute old = tourRouteMapper.selectById(route.getId(), null);
        if (old == null) {
            return Result.error("路线不存在");
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
    public Result<?> delete(Long id) {
        // delete 方法只需要检查记录是否存在，不需要 isCollect 信息，传入 null
        TourRoute old = tourRouteMapper.selectById(id, null);
        if (old == null) {
            return Result.error("路线不存在");
        }
        int deleted = tourRouteMapper.delete(id);
        return deleted > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    @Override
    public Result<PageResult<TourRoute>> list(Integer page, Integer limit, String name, Integer bizStatus, Long companyId) {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        
        int p = (page == null || page < 1) ? 1 : page;
        int l = (limit == null || limit < 1) ? 10 : limit;
        int offset = (p - 1) * l;
        List<TourRoute> list = tourRouteMapper.page(name, bizStatus, companyId, userId, offset, l);
        int total = tourRouteMapper.count(name, bizStatus, companyId);
        PageResult<TourRoute> pr = new PageResult<>();
        pr.setTotal(total);
        pr.setRecords(list);
        return Result.success(pr);
    }

    @Override
    public TourRoute getById(Long id) {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        return tourRouteMapper.selectById(id, userId);
    }
}

