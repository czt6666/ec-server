package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.UserCollect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserCollectMapper {

    int insert(UserCollect collect);

    int delete(@Param("userId") Long userId,
               @Param("targetType") String targetType,
               @Param("targetId") String targetId);

    int exists(@Param("userId") Long userId,
               @Param("targetType") String targetType,
               @Param("targetId") String targetId);

    UserCollect selectOne(@Param("userId") Long userId,
                          @Param("targetType") String targetType,
                          @Param("targetId") String targetId);

    List<UserCollect> page(@Param("userId") Long userId,
                           @Param("targetType") String targetType,
                           @Param("currentUserId") Long currentUserId,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    int count(@Param("userId") Long userId,
              @Param("targetType") String targetType);

    int countByTarget(@Param("targetType") String targetType,
                      @Param("targetId") String targetId);

    List<com.bistu.ecadmin.pojo.UserCollectHotspot> hotspotPage(@Param("targetType") String targetType,
                                                                @Param("days") Integer days,
                                                                @Param("userId") Long userId,
                                                                @Param("offset") int offset,
                                                                @Param("limit") int limit);

    int hotspotCount(@Param("targetType") String targetType,
                     @Param("days") Integer days);
}

