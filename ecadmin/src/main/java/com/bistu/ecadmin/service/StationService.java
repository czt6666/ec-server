package com.bistu.ecadmin.service;

import com.bistu.ecadmin.dao.DTO.StationPageQueryDTO;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Station;

public interface StationService {

    /**
     * 分页查询驿站列表
     */
    PageResult page(StationPageQueryDTO dto);

    /**
     * 根据ID查询驿站详情
     */
    Station getById(Long id);

    /**
     * 新增驿站
     */
    boolean add(Station station);

    /**
     * 更新驿站
     */
    boolean update(Station station);

    /**
     * 删除驿站
     */
    boolean deleteById(Long id);
}

