package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.RouteThemeMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.RouteTheme;
import com.bistu.ecadmin.service.RouteThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteThemeServiceImpl implements RouteThemeService {

    @Autowired
    private RouteThemeMapper routeThemeMapper;

    @Override
    public Result<?> create(RouteTheme routeTheme) {
        RouteTheme existed = routeThemeMapper.selectByName(routeTheme.getThemeName());
        if (existed != null) {
            return Result.error("主题名称已存在");
        }
        if (routeTheme.getSort() == null) {
            routeTheme.setSort(0);
        }
        if (routeTheme.getStatus() == null) {
            routeTheme.setStatus(1);
        }
        int inserted = routeThemeMapper.insert(routeTheme);
        return inserted > 0 ? Result.success("创建成功") : Result.error("创建失败");
    }

    @Override
    public Result<?> update(RouteTheme routeTheme) {
        RouteTheme old = routeThemeMapper.selectById(routeTheme.getId());
        if (old == null) {
            return Result.error("主题不存在");
        }
        if (routeTheme.getThemeName() != null && !routeTheme.getThemeName().equals(old.getThemeName())) {
            RouteTheme existed = routeThemeMapper.selectByName(routeTheme.getThemeName());
            if (existed != null) {
                return Result.error("主题名称已存在");
            }
        }
        int updated = routeThemeMapper.update(routeTheme);
        return updated > 0 ? Result.success("更新成功") : Result.error("更新失败");
    }

    @Override
    public Result<?> delete(Long id) {
        RouteTheme old = routeThemeMapper.selectById(id);
        if (old == null) {
            return Result.error("主题不存在");
        }
        int deleted = routeThemeMapper.delete(id);
        return deleted > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    @Override
    public Result<PageResult<RouteTheme>> list(Integer page, Integer limit, String themeName, Integer status) {
        int p = (page == null || page < 1) ? 1 : page;
        int l = (limit == null || limit < 1) ? 10 : limit;
        int offset = (p - 1) * l;
        List<RouteTheme> list = routeThemeMapper.page(themeName, status, offset, l);
        int total = routeThemeMapper.count(themeName, status);
        PageResult<RouteTheme> pr = new PageResult<>();
        pr.setTotal(total);
        pr.setRecords(list);
        return Result.success(pr);
    }
}



