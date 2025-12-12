package com.bistu.ecadmin.pojo;

import lombok.Data;

@Data
public class UserCollectHotspot {
    private String targetType;
    private String targetId;
    private String targetName;  // 对象名称
    private Long collectCount;  // 收藏总数
    private Boolean isCollected; // 当前用户是否已收藏（需要传递userId时才有值）
}

