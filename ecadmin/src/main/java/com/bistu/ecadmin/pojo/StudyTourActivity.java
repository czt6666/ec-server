package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudyTourActivity {
    private Long id;
    private String activityName;
    private Long planId;
    private LocalDate applyStartDate;
    private LocalDate applyEndDate;
    private LocalDate activityStartDate;
    private LocalDate activityEndDate;
    private BigDecimal price;
    private Integer recruitNum;
    private Integer registeredNum;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}