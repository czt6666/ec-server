package com.bistu.ecadmin.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 小程序用户实体类
 * 用于区分后台管理用户和小程序用户
 */
@Data
public class MiniUser {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 手机号（唯一）
     */
    private String phone;
    
    /**
     * 用户名（唯一）
     */
    private String username;
    
    /**
     * 密码（加密存储）
     */
    private String password;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 性别：0=未知，1=男，2=女
     */
    private Integer gender;
    
    /**
     * 删除状态：0=禁用，1=启用，2=已删除
     */
    private Integer deleteStatus;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
}

