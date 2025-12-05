package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.StudyTourType;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;

import java.util.List;

/**
 * 研学类型服务接口
 */
public interface StudyTourTypeService {

    /**
     * 创建研学类型
     */
    Result createStudyTourType(StudyTourType studyTourType);

    /**
     * 查询研学类型列表
     */
    Result<PageResult> listStudyTourTypes(String typeName, Integer status, int page, int pageSize);

    /**
     * 更新研学类型
     */
    Result updateStudyTourType(StudyTourType studyTourType);

    /**
     * 删除研学类型
     */
    Result deleteStudyTourType(Long id);

    /**
     * 更新研学类型状态
     */
    Result updateStatus(Long id, Integer status);

    /**
     * 获取所有启用的研学类型
     */
    Result<List<StudyTourType>> getAllEnabledTypes();
}