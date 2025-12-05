package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.StudyTourBase;
import com.bistu.ecadmin.pojo.StudyTourType;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;

import java.util.List;

public interface StudyTourBaseService {

    /**
     * 创建研学基地
     */
    Result createStudyTourBase(StudyTourBase studyTourBase);

    /**
     * 查询研学基地列表
     */
    Result<PageResult<StudyTourBase>> listStudyTourBases(String baseName, String operationUnit, Integer businessStatus, int page, int pageSize);

    /**
     * 更新研学基地
     */
    Result updateStudyTourBase(StudyTourBase studyTourBase);

    /**
     * 删除研学基地
     */
    Result deleteStudyTourBase(Long id);

    /**
     * 保存基地与研学类型的关联关系
     */
    Result saveBaseTypes(Long baseId, List<Long> typeIds);

    /**
     * 获取基地关联的研学类型
     */
    Result<List<StudyTourType>> getAssociatedTypes(Long baseId);

    /**
     * 查询所有研学基地列表（用于下拉选择）
     */
    Result<List<StudyTourBase>> listStudyTourBases();
}