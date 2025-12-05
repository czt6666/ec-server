package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 养老服务模式实体类
 */
@Data
public class ElderlyServiceMode {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 服务模式名称（机构住养/日间照料/上门服务/综合型）
     */
    private String modeName;

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