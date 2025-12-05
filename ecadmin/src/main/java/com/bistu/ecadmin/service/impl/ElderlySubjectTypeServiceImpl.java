package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.ElderlySubjectTypeDao;
import com.bistu.ecadmin.pojo.ElderlySubjectType;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.ElderlySubjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElderlySubjectTypeServiceImpl implements ElderlySubjectTypeService {
    
    @Autowired
    private ElderlySubjectTypeDao elderlySubjectTypeDao;
    
    @Override
    public Result<?> createSubjectType(ElderlySubjectType elderlySubjectType) {
        // 检查类型名称是否已存在
        ElderlySubjectType existingType = elderlySubjectTypeDao.selectByTypeName(elderlySubjectType.getTypeName());
        if (existingType != null) {
            return Result.error("该主体类型名称已存在");
        }
        
        // 设置默认值
        if (elderlySubjectType.getSort() == null) {
            elderlySubjectType.setSort(0);
        }
        if (elderlySubjectType.getStatus() == null) {
            elderlySubjectType.setStatus(1);
        }
        
        // 插入数据
        int result = elderlySubjectTypeDao.insert(elderlySubjectType);
        if (result > 0) {
            return Result.success("创建成功");
        } else {
            return Result.error("创建失败");
        }
    }
    
    @Override
    public Result<PageResult> listSubjectTypes(Integer page, Integer pageSize, String typeName, Integer status) {
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        
        // 查询列表
        List<ElderlySubjectType> list = elderlySubjectTypeDao.selectList(typeName, status, offset, pageSize);
        
        // 查询总数
        int total = elderlySubjectTypeDao.count(typeName, status);
        
        // 封装分页结果
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setRecords(list);
        
        return Result.success(pageResult);
    }
    
    @Override
    public Result<?> updateSubjectType(ElderlySubjectType elderlySubjectType) {
        // 检查类型是否存在
        ElderlySubjectType existingType = elderlySubjectTypeDao.selectById(elderlySubjectType.getId());
        if (existingType == null) {
            return Result.error("该主体类型不存在");
        }
        
        // 如果修改了名称，需要检查新名称是否已存在
        if (!existingType.getTypeName().equals(elderlySubjectType.getTypeName())) {
            ElderlySubjectType typeWithName = elderlySubjectTypeDao.selectByTypeName(elderlySubjectType.getTypeName());
            if (typeWithName != null) {
                return Result.error("该主体类型名称已存在");
            }
        }
        
        // 更新数据
        int result = elderlySubjectTypeDao.update(elderlySubjectType);
        if (result > 0) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    @Override
    public Result<?> deleteSubjectType(Long id) {
        // 检查类型是否存在
        ElderlySubjectType existingType = elderlySubjectTypeDao.selectById(id);
        if (existingType == null) {
            return Result.error("该主体类型不存在");
        }
        
        // 删除数据
        int result = elderlySubjectTypeDao.deleteById(id);
        if (result > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
    
    @Override
    public Result<?> updateStatus(Long id, Integer status) {
        // 检查类型是否存在
        ElderlySubjectType existingType = elderlySubjectTypeDao.selectById(id);
        if (existingType == null) {
            return Result.error("该主体类型不存在");
        }
        
        // 更新状态
        int result = elderlySubjectTypeDao.updateStatus(id, status);
        if (result > 0) {
            return Result.success("状态更新成功");
        } else {
            return Result.error("状态更新失败");
        }
    }
}