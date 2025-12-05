package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.ElderlyServiceModeDao;
import com.bistu.ecadmin.pojo.ElderlyServiceMode;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.ElderlyServiceModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElderlyServiceModeServiceImpl implements ElderlyServiceModeService {
    
    @Autowired
    private ElderlyServiceModeDao elderlyServiceModeDao;
    
    @Override
    public Result<?> createServiceMode(ElderlyServiceMode elderlyServiceMode) {
        // 检查模式名称是否已存在
        ElderlyServiceMode existingMode = elderlyServiceModeDao.selectByModeName(elderlyServiceMode.getModeName());
        if (existingMode != null) {
            return Result.error("该服务模式名称已存在");
        }
        
        // 设置默认值
        if (elderlyServiceMode.getSort() == null) {
            elderlyServiceMode.setSort(0);
        }
        if (elderlyServiceMode.getStatus() == null) {
            elderlyServiceMode.setStatus(1);
        }
        
        // 插入数据
        int result = elderlyServiceModeDao.insert(elderlyServiceMode);
        if (result > 0) {
            return Result.success("创建成功");
        } else {
            return Result.error("创建失败");
        }
    }
    
    @Override
    public Result<PageResult> listServiceModes(Integer page, Integer pageSize, String modeName, Integer status) {
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        
        // 查询列表
        List<ElderlyServiceMode> list = elderlyServiceModeDao.selectList(modeName, status, offset, pageSize);
        
        // 查询总数
        int total = elderlyServiceModeDao.count(modeName, status);
        
        // 封装分页结果
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setRecords(list);
        
        return Result.success(pageResult);
    }
    
    @Override
    public Result<?> updateServiceMode(ElderlyServiceMode elderlyServiceMode) {
        // 检查模式是否存在
        ElderlyServiceMode existingMode = elderlyServiceModeDao.selectById(elderlyServiceMode.getId());
        if (existingMode == null) {
            return Result.error("该服务模式不存在");
        }
        
        // 如果修改了名称，需要检查新名称是否已存在
        if (!existingMode.getModeName().equals(elderlyServiceMode.getModeName())) {
            ElderlyServiceMode modeWithName = elderlyServiceModeDao.selectByModeName(elderlyServiceMode.getModeName());
            if (modeWithName != null) {
                return Result.error("该服务模式名称已存在");
            }
        }
        
        // 更新数据
        int result = elderlyServiceModeDao.update(elderlyServiceMode);
        if (result > 0) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    @Override
    public Result<?> deleteServiceMode(Long id) {
        // 检查模式是否存在
        ElderlyServiceMode existingMode = elderlyServiceModeDao.selectById(id);
        if (existingMode == null) {
            return Result.error("该服务模式不存在");
        }
        
        // 删除数据
        int result = elderlyServiceModeDao.deleteById(id);
        if (result > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
    
    @Override
    public Result<?> updateStatus(Long id, Integer status) {
        // 检查模式是否存在
        ElderlyServiceMode existingMode = elderlyServiceModeDao.selectById(id);
        if (existingMode == null) {
            return Result.error("该服务模式不存在");
        }
        
        // 更新状态
        int result = elderlyServiceModeDao.updateStatus(id, status);
        if (result > 0) {
            return Result.success("状态更新成功");
        } else {
            return Result.error("状态更新失败");
        }
    }
}