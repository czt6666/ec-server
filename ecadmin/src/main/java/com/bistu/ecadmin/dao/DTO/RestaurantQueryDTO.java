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
    private Long userId;
}
