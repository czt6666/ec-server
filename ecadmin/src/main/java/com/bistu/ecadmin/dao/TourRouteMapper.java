package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.TourRoute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TourRouteMapper {

    int insert(TourRoute route);

    int update(TourRoute route);

    int delete(@Param("id") Long id);

    TourRoute selectById(@Param("id") Long id);

    int countByName(@Param("name") String name, @Param("excludeId") Long excludeId);

    List<TourRoute> page(@Param("name") String name,
                         @Param("bizStatus") Integer bizStatus,
                         @Param("offset") int offset,
                         @Param("limit") int limit);

    int count(@Param("name") String name, @Param("bizStatus") Integer bizStatus);
}

