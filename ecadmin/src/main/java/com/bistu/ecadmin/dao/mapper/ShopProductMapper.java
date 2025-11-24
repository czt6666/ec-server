package com.bistu.ecadmin.dao.mapper;



import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopProductMapper {

    /**
     * 查询指定店铺的商品基础信息
     */
    List<JSONObject> listProductsByShopId(@Param("shopId") Long shopId,
                                          @Param("status") Integer status);
}
