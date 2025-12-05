package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.StudyTourBaseDao;
import com.bistu.ecadmin.dao.StudyTourTypeDao;
import com.bistu.ecadmin.pojo.StudyTourBase;
import com.bistu.ecadmin.pojo.StudyTourType;
import com.bistu.ecadmin.pojo.StudyTourBaseTypeRel;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.StudyTourBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StudyTourBaseServiceImpl implements StudyTourBaseService {

    @Autowired
    private StudyTourBaseDao studyTourBaseDao;

    @Autowired
    private StudyTourTypeDao studyTourTypeDao;

    /**
     * 创建研学基地
     */
    @Override
    @Transactional
    public Result createStudyTourBase(StudyTourBase studyTourBase) {
        // 设置创建时间和更新时间
        studyTourBase.setCreateTime(LocalDateTime.now());
        studyTourBase.setUpdateTime(LocalDateTime.now());

        // 插入数据
        studyTourBaseDao.insert(studyTourBase);
        
        return Result.success(studyTourBase.getId());
    }

    /**
     * 查询研学基地列表
     */
    @Override
    public Result<PageResult<StudyTourBase>> listStudyTourBases(String baseName, String operationUnit, Integer businessStatus, int page, int pageSize) {
        // 计算总记录数
        int total = studyTourBaseDao.count(baseName, operationUnit, businessStatus);

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 分页查询
        List<StudyTourBase> list = studyTourBaseDao.selectList(baseName, operationUnit, businessStatus, offset, pageSize);

        // 封装分页结果
        PageResult<StudyTourBase> pageResult = new PageResult<>(total, list);
        return Result.success(pageResult);
    }

    /**
     * 更新研学基地
     */
    @Override
    public Result updateStudyTourBase(StudyTourBase studyTourBase) {
        // 检查要更新的基地是否存在
        StudyTourBase existing = studyTourBaseDao.selectById(studyTourBase.getId());
        if (existing == null) {
            return Result.error("研学基地不存在");
        }

        // 设置更新时间
        studyTourBase.setUpdateTime(LocalDateTime.now());

        // 更新数据
        studyTourBaseDao.update(studyTourBase);
        return Result.success();
    }

    /**
     * 删除研学基地
     */
    @Override
    @Transactional
    public Result deleteStudyTourBase(Long id) {
        // 检查要删除的基地是否存在
        StudyTourBase existing = studyTourBaseDao.selectById(id);
        if (existing == null) {
            return Result.error("研学基地不存在");
        }

        // 删除基地与类型关联关系
        studyTourBaseDao.deleteBaseTypeRelsByBaseId(id);

        // 删除基地数据
        studyTourBaseDao.deleteById(id);
        return Result.success();
    }

    /**
     * 保存基地与研学类型的关联关系
     */
    @Override
    @Transactional
    public Result saveBaseTypes(Long baseId, List<Long> typeIds) {
        // 检查基地是否存在
        StudyTourBase existing = studyTourBaseDao.selectById(baseId);
        if (existing == null) {
            return Result.error("研学基地不存在");
        }

        // 删除原有的关联关系
        studyTourBaseDao.deleteBaseTypeRelsByBaseId(baseId);

        // 插入新的关联关系
        if (typeIds != null && !typeIds.isEmpty()) {
            for (Long typeId : typeIds) {
                // 检查类型是否存在
                StudyTourType type = studyTourTypeDao.selectById(typeId);
                if (type != null) {
                    StudyTourBaseTypeRel rel = new StudyTourBaseTypeRel();
                    rel.setBaseId(baseId);
                    rel.setTypeId(typeId);
                    rel.setCreateTime(LocalDateTime.now());
                    studyTourBaseDao.insertBaseTypeRel(rel);
                }
            }
        }

        return Result.success();
    }

    /**
     * 获取基地关联的研学类型
     */
    @Override
    public Result<List<StudyTourType>> getAssociatedTypes(Long baseId) {
        // 检查基地是否存在
        StudyTourBase existing = studyTourBaseDao.selectById(baseId);
        if (existing == null) {
            return Result.error("研学基地不存在");
        }

        // 查询关联的类型
        List<StudyTourType> types = studyTourBaseDao.selectTypesByBaseId(baseId);
        return Result.success(types);
    }

    /**
     * 查询所有研学基地列表
     */
    @Override
    public Result<List<StudyTourBase>> listStudyTourBases() {
        List<StudyTourBase> list = studyTourBaseDao.selectAll();
        return Result.success(list);
    }
}