package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.util.Date;

/**
 * 操作日志实体类
 */
@Data
public class OperateLog {
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 操作用户ID
     */
    private Long userId;
    
    /**
     * 操作用户名
     */
    private String username;
    
    /**
     * 操作类型（如：新增、修改、删除等）
     */
    private String operation;
    
    /**
     * 请求方法（完整类名.方法名）
     */
    private String method;
    
    /**
     * 请求参数
     */
    private String requestParams;
    
    /**
     * 响应结果
     */
    private String responseResult;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * 操作时间
     */
    private Date createTime;
    
    /**
     * 耗时（毫秒）
     */
    private Long costTime;
    
    /**
     * 是否成功（0-失败，1-成功）
     */
    private Integer success;
    
    /**
     * 错误信息
     */
    private String errorMessage;
}