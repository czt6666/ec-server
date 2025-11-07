package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.pojo.Cart;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {
    int insertOrTouch(@Param("userId") Integer userId, @Param("skuId") Integer skuId);

    /**
     * 查询某用户购物车列表
     */
    List<Cart> selectByUserId(@Param("userId") Integer userId);

    /**
     * 删除购物车中的某个 SKU
     */
    int deleteByUserAndSku(@Param("userId") Integer userId, @Param("skuId") Integer skuId);
    // 新增
    List<Cart> selectByUserIdPaged(@Param("userId") int userId,
                                   @Param("offset") int offset,
                                   @Param("pageSize") int pageSize);

    int countByUserId(@Param("userId") int userId);
    List<Cart> selectAllPaged(@Param("offset") int offset,
                                   @Param("pageSize") int pageSize);

    int countAll();
}
