package com.bistu.ecadmin.pojo;

import lombok.Data;

@Data
public class UserCollectHotspot {
    private String targetType;
    private String targetId;
    private String targetName;  // 对象名称
    private Long collectCount;
}

