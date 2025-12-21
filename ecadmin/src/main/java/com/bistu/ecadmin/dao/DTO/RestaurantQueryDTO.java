package com.bistu.ecadmin.dao.DTO;

import lombok.Data;

@Data
public class RestaurantQueryDTO {
    private String name;
    private Integer status;
    private Integer villageId;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer offset;
    private Long userId;  // 后端登录用户ID（用于限权）
    private Long miniProgramUserId;  // 小程序用户ID（用于 isCollect 计算）
}
