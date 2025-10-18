package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.DTO.VillagePageQueryDTO;
import com.bistu.ecadmin.dao.mapper.VillageMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.village;
import com.bistu.ecadmin.service.village_service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class village_serviceIml implements village_service {
@Autowired
private VillageMapper villageMapper;

    @Override
    public PageResult page(VillagePageQueryDTO dto) {
        int pageNum = dto.getPage() == null || dto.getPage() < 1 ? 1 : dto.getPage();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 10 : dto.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<village> page = villageMapper.pageQuery(); // 下一条SQL自动分页
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<village> list(village village) {
        return villageMapper.list(village);
    }

    @Override
    public void add(village village) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        village.setCreateTime(now);
        village.setUpdateTime(now);

        // 设置默认管理人数
        if (village.getManagerCount() == null) {
            village.setManagerCount(150);
        }

        villageMapper.insert(village);
    }

    @Override
    public void update(village village) {
        // 设置更新时间
        village.setUpdateTime(LocalDateTime.now());
        villageMapper.update(village);
    }

    @Override
    public village getById(Integer id) {
        return villageMapper.getById(id);
    }

    @Override
    public void delete(Integer id) {
        // 检查村庄是否存在
        village village = villageMapper.getById(id);
        if (village == null) {
            throw new RuntimeException("村庄不存在");
        }
        villageMapper.delete(id);
    }

}

