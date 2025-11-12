package com.bistu.ecadmin.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Role {
    private Long id;
    private String roleName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleteStatus;
}
