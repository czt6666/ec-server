package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.ElderlySubjectType;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;

public interface ElderlySubjectTypeService {
    
    /**
     * 创建养老服务主体类型
     * @param elderlySubjectType 养老服务主体类型对象
     * @return 结果
     */
    Result<?> createSubjectType(ElderlySubjectType elderlySubjectType);
    
    /**
     * 获取养老服务主体类型列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param typeName 类型名称
     * @param status 状态
     * @return 分页结果
     */
    Result<PageResult> listSubjectTypes(Integer page, Integer pageSize, String typeName, Integer status);
    
    /**
     * 更新养老服务主体类型
     * @param elderlySubjectType 养老服务主体类型对象
     * @return 结果
     */
    Result<?> updateSubjectType(ElderlySubjectType elderlySubjectType);
    
    /**
     * 删除养老服务主体类型
     * @param id 养老服务主体类型ID
     * @return 结果
     */
    Result<?> deleteSubjectType(Long id);
    
    /**
     * 更新养老服务主体类型状态
     * @param id 养老服务主体类型ID
     * @param status 状态
     * @return 结果
     */
    Result<?> updateStatus(Long id, Integer status);
}