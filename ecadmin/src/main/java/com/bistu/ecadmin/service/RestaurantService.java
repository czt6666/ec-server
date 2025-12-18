package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Restaurant;
import com.bistu.ecadmin.dao.DTO.RestaurantQueryDTO;

import java.util.List;

public interface RestaurantService {

    PageResult list(RestaurantQueryDTO dto);

    Restaurant getById(Long id, Long userId);

    void create(Restaurant restaurant);

    void update(Restaurant restaurant);

    void delete(Long id);

    List<String> listNamesByUser(Long userId);

    Long getIdByName(String name);
    
    List<Restaurant> listByUser(Long userId);
}
