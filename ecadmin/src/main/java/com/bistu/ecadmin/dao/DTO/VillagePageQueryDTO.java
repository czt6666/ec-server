package com.bistu.ecadmin.dao.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VillagePageQueryDTO {
    // 分页参数
    private Integer page;      // 从1开始
    private Integer pageSize;


}