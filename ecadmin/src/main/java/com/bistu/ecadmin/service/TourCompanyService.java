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
    
    /**
     * 上架旅游公司（仅管理员，将营业状态改为1-营业中）
     */
    boolean publish(Long id);
    
    /**
     * 下架旅游公司（仅管理员，将营业状态改为3-已注销）
     */
    boolean unpublish(Long id);
}

