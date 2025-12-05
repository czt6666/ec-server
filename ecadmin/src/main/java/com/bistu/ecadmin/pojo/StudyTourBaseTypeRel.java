package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 基地与研学类型关联实体类
 */
@Data
public class StudyTourBaseTypeRel {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 基地ID（关联研学基地表）
     */
    private Long baseId;

    /**
     * 研学类型ID（关联研学类型表）
     */
    private Long typeId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}