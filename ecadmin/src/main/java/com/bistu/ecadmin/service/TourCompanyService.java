package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.TourCompany;

public interface TourCompanyService {

    Result<?> create(TourCompany company);

    Result<?> update(TourCompany company);

    Result<?> delete(Long id);

    Result<PageResult<TourCompany>> list(Integer page, Integer limit, String name, Integer status);

    TourCompany getById(Long id);
}

