package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.UserCollectMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.UserCollect;
import com.bistu.ecadmin.service.UserCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class UserCollectServiceImpl implements UserCollectService {

    private static final List<String> ALLOWED_TYPES = Arrays.asList(
            "tour_route",      // 旅游线路
            "elderly_station", // 养老驿站
            "study_plan",      // 研学方案
            "study_activity",  // 研学活动
            "homestay",        // 民宿
            "product",         // 农产品/商品
            "restaurant"       // 餐饮/菜品/店铺
    );

    @Autowired
    private UserCollectMapper userCollectMapper;

    @Override
    public Result<?> create(UserCollect collect) {
        // userId可以为0（匿名用户），但不能为null
        if (collect.getUserId() == null || !StringUtils.hasText(collect.getTargetType()) || !StringUtils.hasText(collect.getTargetId())) {
            return Result.error("userId/targetType/targetId 不能为空");
        }
        if (!ALLOWED_TYPES.contains(collect.getTargetType())) {
            return Result.error("targetType 非法");
        }
        // 防重复收藏（包括匿名用户）
        if (userCollectMapper.exists(collect.getUserId(), collect.getTargetType(), collect.getTargetId()) > 0) {
            return Result.success("已收藏");
        }
        try {
            int inserted = userCollectMapper.insert(collect);
            return inserted > 0 ? Result.success("收藏成功") : Result.error("收藏失败");
        } catch (DuplicateKeyException e) {
            return Result.success("已收藏");
        }
    }

    @Override
    public Result<?> delete(Long userId, String targetType, String targetId) {
        if (userId == null || !StringUtils.hasText(targetType) || !StringUtils.hasText(targetId)) {
            return Result.error("userId/targetType/targetId 不能为空");
        }
        int deleted = userCollectMapper.delete(userId, targetType, targetId);
        return deleted > 0 ? Result.success("已取消收藏") : Result.error("未找到记录");
    }

    @Override
    public Result<PageResult<UserCollect>> list(Integer page, Integer limit, Long userId, String targetType, Long currentUserId) {
        int p = (page == null || page < 1) ? 1 : page;
        int l = (limit == null || limit < 1) ? 10 : limit;
        int offset = (p - 1) * l;
        // 如果传了userId查询某个用户的收藏列表，则currentUserId默认为userId（列表中的记录都是该用户收藏的）
        Long currentUid = (currentUserId != null) ? currentUserId : userId;
        List<UserCollect> list = userCollectMapper.page(userId, targetType, currentUid, offset, l);
        int total = userCollectMapper.count(userId, targetType);
        PageResult<UserCollect> pr = new PageResult<>();
        pr.setRecords(list);
        pr.setTotal(total);
        return Result.success(pr);
    }

    @Override
    public Result<UserCollect> get(Long userId, String targetType, String targetId) {
        if (userId == null || !StringUtils.hasText(targetType) || !StringUtils.hasText(targetId)) {
            return Result.error("userId/targetType/targetId 不能为空");
        }
        if (!ALLOWED_TYPES.contains(targetType)) {
            return Result.error("targetType 非法");
        }
        UserCollect uc = userCollectMapper.selectOne(userId, targetType, targetId);
        return Result.success(uc);
    }

    @Override
    public Result<Integer> countByTarget(String targetType, String targetId) {
        if (!StringUtils.hasText(targetType) || !StringUtils.hasText(targetId)) {
            return Result.error("targetType/targetId 不能为空");
        }
        if (!ALLOWED_TYPES.contains(targetType)) {
            return Result.error("targetType 非法");
        }
        int count = userCollectMapper.countByTarget(targetType, targetId);
        return Result.success(count);
    }

    @Override
    public Result<PageResult<com.bistu.ecadmin.pojo.UserCollectHotspot>> hotspot(Integer page, Integer limit, String targetType, Integer days, Long userId) {
        // 如果传了 targetType，则验证其合法性
        if (StringUtils.hasText(targetType) && !ALLOWED_TYPES.contains(targetType)) {
            return Result.error("targetType 非法");
        }
        int p = (page == null || page < 1) ? 1 : page;
        int l = (limit == null || limit < 1) ? 10 : limit;
        int offset = (p - 1) * l;
        List<com.bistu.ecadmin.pojo.UserCollectHotspot> list = userCollectMapper.hotspotPage(targetType, days, userId, offset, l);
        int total = userCollectMapper.hotspotCount(targetType, days);
        PageResult<com.bistu.ecadmin.pojo.UserCollectHotspot> pr = new PageResult<>();
        pr.setRecords(list);
        pr.setTotal(total);
        return Result.success(pr);
    }
}

