package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.RouteTypeMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.RouteType;
import com.bistu.ecadmin.service.RouteTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteTypeServiceImpl implements RouteTypeService {

    @Autowired
    private RouteTypeMapper routeTypeMapper;

    @Override
    public Result<?> create(RouteType routeType) {
        RouteType existed = routeTypeMapper.selectByName(routeType.getTypeName());
        if (existed != null) {
            return Result.error("类型名称已存在");
        }
        if (routeType.getSort() == null) {
            routeType.setSort(0);
        }
        if (routeType.getStatus() == null) {
            routeType.setStatus(1);
        }
        int inserted = routeTypeMapper.insert(routeType);
        return inserted > 0 ? Result.success("创建成功") : Result.error("创建失败");
    }

    @Override
    public Result<?> update(RouteType routeType) {
        RouteType old = routeTypeMapper.selectById(routeType.getId());
        if (old == null) {
            return Result.error("类型不存在");
        }
        if (routeType.getTypeName() != null && !routeType.getTypeName().equals(old.getTypeName())) {
            RouteType existed = routeTypeMapper.selectByName(routeType.getTypeName());
            if (existed != null) {
                return Result.error("类型名称已存在");
            }
        }
        int updated = routeTypeMapper.update(routeType);
        return updated > 0 ? Result.success("更新成功") : Result.error("更新失败");
    }

    @Override
    public Result<?> delete(Long id) {
        RouteType old = routeTypeMapper.selectById(id);
        if (old == null) {
            return Result.error("类型不存在");
        }
        int deleted = routeTypeMapper.delete(id);
        return deleted > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    @Override
    public Result<PageResult<RouteType>> list(Integer page, Integer limit, String typeName, Integer status) {
        int p = (page == null || page < 1) ? 1 : page;
        int l = (limit == null || limit < 1) ? 10 : limit;
        int offset = (p - 1) * l;
        List<RouteType> list = routeTypeMapper.page(typeName, status, offset, l);
        int total = routeTypeMapper.count(typeName, status);
        PageResult<RouteType> pr = new PageResult<>();
        pr.setTotal(total);
        pr.setRecords(list);
        return Result.success(pr);
    }
}









