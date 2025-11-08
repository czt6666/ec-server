package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.pojo.Cart;
import com.bistu.ecadmin.pojo.CartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {
    // 幂等加入购物车：若存在则触达更新时间
    void insertOrTouch(@Param("userId") int userId, @Param("skuId") int skuId);

    // 更新商品加入购物车次数
    void incrementCartAddCount(@Param("spuId") Long spuId);

    // 根据 sku_id 查询 spu_id
    Long getSpuIdBySkuId(@Param("skuId") int skuId);

    // 查询某用户购物车列表
    List<Cart> selectByUserId(@Param("userId") int userId);

    // 查询某用户购物车列表（关联商品信息）- 返回 CartVO
    List<CartVO> selectByUserIdWithProduct(@Param("userId") int userId);

    // 删除购物车中的某个 SKU
    int deleteByUserAndSku(@Param("userId") int userId, @Param("skuId") int skuId);

    // 分页查询某用户购物车列表
    List<Cart> selectByUserIdPaged(@Param("userId") int userId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 分页查询某用户购物车列表（关联商品信息）- 返回 CartVO
    List<CartVO> selectByUserIdPagedWithProduct(@Param("userId") int userId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询某用户购物车总数
    int countByUserId(@Param("userId") int userId);

    // 分页查询所有购物车列表
    List<Cart> selectAllPaged(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 分页查询所有购物车列表（关联商品信息）- 返回 CartVO
    List<CartVO> selectAllPagedWithProduct(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询所有购物车总数
    int countAll();
    Integer getSpuStatusBySkuId(int skuId);
}
