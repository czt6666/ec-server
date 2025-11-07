package com.bistu.ecadmin.dao.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CartPageDTO {
    @ApiModelProperty("页码")
    private Integer page;

    @ApiModelProperty("每页大小")
    private Integer pageSize;
    @ApiModelProperty("用户ID，不必填")
    private Integer userId;
}
