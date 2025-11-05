package com.bistu.ecadmin.dao.DTO;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("购物车DTO")
public class CartAddDTO {
	private Integer userId;
	private Integer skuId;


}
