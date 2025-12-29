package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.TourRoute;

public interface TourRouteService {

    Result<?> create(TourRoute route);

    Result<?> update(TourRoute route);

    Result<?> delete(Long id);

    Result<PageResult<TourRoute>> list(Integer page, Integer limit, String name, Integer bizStatus, Long companyId);

    TourRoute getById(Long id);
}

