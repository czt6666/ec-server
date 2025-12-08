package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.RouteTheme;

public interface RouteThemeService {

    Result<?> create(RouteTheme routeTheme);

    Result<?> update(RouteTheme routeTheme);

    Result<?> delete(Long id);

    Result<PageResult<RouteTheme>> list(Integer page, Integer limit, String themeName, Integer status);
}



