package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 研学类型实体类
 */
@Data
public class StudyTourType {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 研学类型名称（红色教育/户外拓展/亲子研学/综合型）
     */
    private String typeName;

    /**
     * 排序（数字越小越靠前）
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