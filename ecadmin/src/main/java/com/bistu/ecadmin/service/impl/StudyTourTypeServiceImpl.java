package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.StudyTourTypeDao;
import com.bistu.ecadmin.pojo.StudyTourType;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.StudyTourTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class StudyTourTypeServiceImpl implements StudyTourTypeService {

    @Autowired
    private StudyTourTypeDao studyTourTypeDao;

    /**
     * 创建研学类型
     */
    @Override
    public Result createStudyTourType(StudyTourType studyTourType) {
        // 检查类型名称是否已存在
        StudyTourType existing = studyTourTypeDao.selectByTypeName(studyTourType.getTypeName());
        if (existing != null) {
            return Result.error("该研学类型已存在");
        }

        // 设置创建时间
        studyTourType.setCreateTime(LocalDateTime.now());

        // 插入数据
        studyTourTypeDao.insert(studyTourType);
        return Result.success();
    }

    /**
     * 查询研学类型列表
     */
    @Override
    public Result<PageResult> listStudyTourTypes(String typeName, Integer status, int page, int pageSize) {
        // 计算总记录数
        int total = studyTourTypeDao.count(typeName, status);

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 分页查询
        List<StudyTourType> list = studyTourTypeDao.selectList(typeName, status, offset, pageSize);

        // 封装分页结果
        PageResult pageResult = new PageResult(total, list);
        return Result.success(pageResult);
    }

    /**
     * 更新研学类型
     */
    @Override
    public Result updateStudyTourType(StudyTourType studyTourType) {
        // 检查要更新的类型是否存在
        StudyTourType existing = studyTourTypeDao.selectById(studyTourType.getId());
        if (existing == null) {
            return Result.error("研学类型不存在");
        }

        // 检查类型名称是否与其他记录冲突
        StudyTourType duplicate = studyTourTypeDao.selectByTypeName(studyTourType.getTypeName());
        if (duplicate != null && !duplicate.getId().equals(studyTourType.getId())) {
            return Result.error("该研学类型已存在");
        }

        // 更新数据
        studyTourTypeDao.update(studyTourType);
        return Result.success();
    }

    /**
     * 删除研学类型
     */
    @Override
    public Result deleteStudyTourType(Long id) {
        // 检查要删除的类型是否存在
        StudyTourType existing = studyTourTypeDao.selectById(id);
        if (existing == null) {
            return Result.error("研学类型不存在");
        }

        // 删除数据
        studyTourTypeDao.deleteById(id);
        return Result.success();
    }

    /**
     * 更新研学类型状态
     */
    @Override
    public Result updateStatus(Long id, Integer status) {
        // 检查要更新的类型是否存在
        StudyTourType existing = studyTourTypeDao.selectById(id);
        if (existing == null) {
            return Result.error("研学类型不存在");
        }

        // 更新状态
        studyTourTypeDao.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 获取所有启用的研学类型
     */
    @Override
    public Result<List<StudyTourType>> getAllEnabledTypes() {
        List<StudyTourType> types = studyTourTypeDao.selectAllEnabledTypes();
        return Result.success(types);
    }
}