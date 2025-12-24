package com.bistu.ecadmin.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 
 * @TableName study_tour_activity
 */
@Data
public class StudyTourActivity implements Serializable {
    private Long id;

    @JsonProperty("planId")
    private Long tourPlanId;

    private String activityName;

    private String applyStartDate;

    private String applyEndDate;

    private String activityStartDate;

    private String activityEndDate;

    private BigDecimal price;

    private Integer recruitNum;

    private Integer registeredNum;

    private Integer status;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private Integer collectNumber;

    private Integer isCollect;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String planName;

    private static final long serialVersionUID = 1L;
}