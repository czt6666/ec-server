package com.bistu.ecadmin.service.impl;


import com.bistu.ecadmin.dao.DTO.VillageHomestayPageQueryDTO;
import com.bistu.ecadmin.dao.mapper.VillageHomestayMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.VillageHomestay;
import com.bistu.ecadmin.service.VillageHomestayService;
import com.bistu.ecadmin.util.UserContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VillageHomestayServiceImpl implements VillageHomestayService {

    @Autowired
    private VillageHomestayMapper villageHomestayMapper;

    @Override
    public PageResult page(VillageHomestayPageQueryDTO dto) {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        dto.setUserId(userId);
        
        int pageNum = dto.getPage() == null || dto.getPage() < 1 ? 1 : dto.getPage();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 10 : dto.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<VillageHomestay> page = villageHomestayMapper.pageQuery(dto);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public VillageHomestay getById(Integer id) {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        return villageHomestayMapper.selectById(id, userId);
    }

    @Override
    public boolean add(VillageHomestay homestay) {
        homestay.setCreateTime(LocalDateTime.now());
        return villageHomestayMapper.insert(homestay) > 0;
    }

    @Override
    public boolean update(VillageHomestay homestay) {
        return villageHomestayMapper.update(homestay) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return villageHomestayMapper.deleteById(id) > 0;
    }

    @Override
    public List<VillageHomestay> getByVillageId(Integer villageId) {
        return villageHomestayMapper.selectByVillageId(villageId);
    }
}
