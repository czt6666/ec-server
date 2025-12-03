package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 养老服务主体类型实体类
 */
@Data
public class ElderlySubjectType {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 主体类型名称（养老机构/社区养老/居家养老/老年医院/养老服务中心）
     */
    private String typeName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态（1：启用；0：禁用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}