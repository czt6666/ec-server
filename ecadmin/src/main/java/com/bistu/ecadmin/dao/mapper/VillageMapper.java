package com.bistu.ecadmin.dao.mapper;


import com.bistu.ecadmin.dao.DTO.VillagePageQueryDTO;
import com.bistu.ecadmin.pojo.village;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
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

}