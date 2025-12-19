package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.RouteType;

public interface RouteTypeService {

    Result<?> create(RouteType routeType);

    Result<?> update(RouteType routeType);

    Result<?> delete(Long id);

    Result<PageResult<RouteType>> list(Integer page, Integer limit, String typeName, Integer status);
}

















