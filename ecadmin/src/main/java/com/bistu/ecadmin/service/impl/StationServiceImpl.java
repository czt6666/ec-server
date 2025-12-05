package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.DTO.StationPageQueryDTO;
import com.bistu.ecadmin.dao.mapper.StationMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Station;
import com.bistu.ecadmin.service.StationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationMapper stationMapper;

    @Override
    public PageResult page(StationPageQueryDTO dto) {
        int pageNum = dto.getPage() == null || dto.getPage() < 1 ? 1 : dto.getPage();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 10 : dto.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<Station> page = stationMapper.pageQuery(dto);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Station getById(Long id) {
        return stationMapper.selectById(id);
    }

    @Override
    public boolean add(Station station) {
        // 名称唯一校验
        if (stationMapper.countByName(station.getName(), null) > 0) {
            throw new IllegalArgumentException("驿站名称已存在");
        }
        // subject_type_id 允许为空，若前端未传则设为 0 以避免数据库非空/外键约束
        if (station.getSubjectTypeId() == null) {
            station.setSubjectTypeId(0L);
        }
        station.setCreateTime(LocalDateTime.now());
        return stationMapper.insert(station) > 0;
    }

    @Override
    public boolean update(Station station) {
        // 名称唯一校验（排除自身）
        if (stationMapper.countByName(station.getName(), station.getId()) > 0) {
            throw new IllegalArgumentException("驿站名称已存在");
        }
        return stationMapper.update(station) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return stationMapper.deleteById(id) > 0;
    }
}

