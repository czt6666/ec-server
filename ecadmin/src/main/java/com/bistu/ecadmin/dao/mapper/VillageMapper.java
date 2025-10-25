package com.bistu.ecadmin.dao.mapper;


import com.bistu.ecadmin.dao.DTO.VillagePageQueryDTO;
import com.bistu.ecadmin.pojo.village;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VillageMapper {

    // 条件分页查询（XML实现）
    Page<village> pageQuery();




    // 简单条件列表（XML实现，常用于导出/下拉等）
    List<village> list(village village);
    // 新增村庄
    void insert(village village);
    // 在 VillageMapper 接口中添加
// 修改村庄
    void update(village village);

    // 根据ID查询村庄
    village getById(Integer id);
    // 删除村庄
    void delete(Integer id);
    // 级联删除相关方法
    @Delete("DELETE FROM village_news WHERE village_name = #{villageName}")
    void deleteVillageNewsByVillageName(@Param("villageName") String villageName);

    @Delete("DELETE FROM village_homestay WHERE village_id = #{villageId}")
    void deleteVillageHomestayByVillageId(@Param("villageId") Integer villageId);

    // 检查关联数据是否存在
    @Select("SELECT COUNT(*) FROM village_news WHERE village_name = #{villageName}")
    int countVillageNewsByVillageName(@Param("villageName") String villageName);

    @Select("SELECT COUNT(*) FROM village_homestay WHERE village_id = #{villageId}")
    int countVillageHomestayByVillageId(@Param("villageId") Integer villageId);

    // 获取关联数据信息
    @Select("SELECT COUNT(*) FROM village_news WHERE village_name = #{villageName}")
    int getVillageNewsCount(@Param("villageName") String villageName);

    @Select("SELECT COUNT(*) FROM village_homestay WHERE village_id = #{villageId}")
    int getVillageHomestayCount(@Param("villageId") Integer villageId);
}