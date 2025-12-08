package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.RouteType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RouteTypeMapper {

    int insert(RouteType routeType);

    int update(RouteType routeType);

    int delete(@Param("id") Long id);

    RouteType selectById(@Param("id") Long id);

    RouteType selectByName(@Param("typeName") String typeName);

    List<RouteType> page(@Param("typeName") String typeName,
                         @Param("status") Integer status,
                         @Param("offset") int offset,
                         @Param("limit") int limit);

    int count(@Param("typeName") String typeName, @Param("status") Integer status);
}



