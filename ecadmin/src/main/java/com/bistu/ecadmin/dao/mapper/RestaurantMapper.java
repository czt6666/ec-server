package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.dao.DTO.RestaurantQueryDTO;
import com.bistu.ecadmin.pojo.Restaurant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RestaurantMapper {

    int insert(Restaurant restaurant);

    int update(Restaurant restaurant);

    int deleteById(@Param("id") Long id);

    Restaurant selectById(@Param("id") Long id, @Param("miniProgramUserId") Long miniProgramUserId);

    List<Restaurant> list(RestaurantQueryDTO dto);

    int count(RestaurantQueryDTO dto);

    List<String> listNamesByUserId(@Param("userId") Long userId);

    Long getIdByName(@Param("name") String name);

    List<Restaurant> listByUserId(@Param("userId") Long userId);

    Integer getMaxSortOrder(@Param("userId") Long userId);
}
