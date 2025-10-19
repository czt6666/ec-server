package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.dao.DTO.VillageHomestayPageQueryDTO;
import com.bistu.ecadmin.pojo.VillageHomestay;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VillageHomestayMapper {
    
    /**
     * 分页查询民宿列表（支持模糊查询）
     */
    Page<VillageHomestay> pageQuery(VillageHomestayPageQueryDTO dto);
    
    /**
     * 根据ID查询民宿详情
     */
    VillageHomestay selectById(@Param("id") Integer id);
    
    /**
     * 新增民宿
     */
    int insert(VillageHomestay homestay);
    
    /**
     * 更新民宿
     */
    int update(VillageHomestay homestay);
    
    /**
     * 删除民宿
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据乡村ID查询民宿列表
     */
    List<VillageHomestay> selectByVillageId(@Param("villageId") Integer villageId);
    
    /**
     * 统计民宿总数
     */
    long count(VillageHomestayPageQueryDTO dto);
}