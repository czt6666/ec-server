package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.RouteTheme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RouteThemeMapper {

    int insert(RouteTheme routeTheme);

    int update(RouteTheme routeTheme);

    int delete(@Param("id") Long id);

    RouteTheme selectById(@Param("id") Long id);

    RouteTheme selectByName(@Param("themeName") String themeName);

    List<RouteTheme> page(@Param("themeName") String themeName,
                          @Param("status") Integer status,
                          @Param("offset") int offset,
                          @Param("limit") int limit);

    int count(@Param("themeName") String themeName, @Param("status") Integer status);
}









