package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.UserCollect;

public interface UserCollectService {

    Result<?> create(UserCollect collect);

    Result<?> delete(Long userId, String targetType, String targetId);

    Result<UserCollect> get(Long userId, String targetType, String targetId);

    Result<PageResult<UserCollect>> list(Integer page, Integer limit, Long userId, String targetType);

    Result<Integer> countByTarget(String targetType, String targetId);

    Result<PageResult<com.bistu.ecadmin.pojo.UserCollectHotspot>> hotspot(Integer page, Integer limit, String targetType, Integer days);
}

