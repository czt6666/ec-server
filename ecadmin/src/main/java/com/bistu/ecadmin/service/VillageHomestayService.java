package com.bistu.ecadmin.service;

import com.bistu.ecadmin.dao.DTO.VillageHomestayPageQueryDTO;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.VillageHomestay;

import java.util.List;

public interface VillageHomestayService {

    /**
     * 分页查询民宿列表
     */
    PageResult page(VillageHomestayPageQueryDTO dto);

    /**
     * 根据ID查询民宿详情
     */
    VillageHomestay getById(Integer id);

    /**
     * 新增民宿
     */
    boolean add(VillageHomestay homestay);

    /**
     * 更新民宿
     */
    boolean update(VillageHomestay homestay);

    /**
     * 删除民宿
     */
    boolean deleteById(Integer id);

    /**
     * 根据乡村ID查询民宿列表
     */
    List<VillageHomestay> getByVillageId(Integer villageId);

    /**
     * 管理员上架民宿（将状态改为1-营业）
     */
    boolean publish(Integer id);

    /**
     * 管理员下架民宿（将状态改为3-已下架）
     */
    boolean unpublish(Integer id);
}
