package com.bistu.ecadmin.service;

import com.bistu.ecadmin.dao.DTO.VillagePageQueryDTO;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.village;

import java.util.List;
import java.util.Map;

public interface village_service {
    // 分页查询：返回 { total, records, page, pageSize }
    PageResult page(VillagePageQueryDTO dto);

    // 简单条件列表（不分页）
    List<village> list(village village);
    // 新增村庄
    void add(village village);
    // 在 VillageService 接口中添加
// 修改村庄
    void update(village village);

    // 根据ID查询村庄
    village getById(Integer id);
    // 删除村庄
    void delete(Integer id);
}
