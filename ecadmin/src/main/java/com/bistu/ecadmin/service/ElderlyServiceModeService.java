package com.bistu.ecadmin.service;

import com.bistu.ecadmin.pojo.ElderlyServiceMode;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;

public interface ElderlyServiceModeService {
    
    /**
     * 创建养老服务模式
     * @param elderlyServiceMode 养老服务模式对象
     * @return 结果对象
     */
    Result<?> createServiceMode(ElderlyServiceMode elderlyServiceMode);
    
    /**
     * 获取养老服务模式列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param modeName 模式名称
     * @param status 状态
     * @return 分页结果对象
     */
    Result<PageResult> listServiceModes(Integer page, Integer pageSize, String modeName, Integer status);
    
    /**
     * 更新养老服务模式
     * @param elderlyServiceMode 养老服务模式对象
     * @return 结果对象
     */
    Result<?> updateServiceMode(ElderlyServiceMode elderlyServiceMode);
    
    /**
     * 删除养老服务模式
     * @param id 养老服务模式ID
     * @return 结果对象
     */
    Result<?> deleteServiceMode(Long id);
    
    /**
     * 更新养老服务模式状态
     * @param id 养老服务模式ID
     * @param status 状态
     * @return 结果对象
     */
    Result<?> updateStatus(Long id, Integer status);
}