package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudyTourPlan {
    private Long id;
    private String planName;
    private Long baseId;
    private String route;
    private String briefIntro;
    private String details;
    private String suitableCrowd;
    private String duration;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long collectNumber;
}