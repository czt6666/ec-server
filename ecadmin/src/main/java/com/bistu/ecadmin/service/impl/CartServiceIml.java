package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.mapper.CartMapper;
import com.bistu.ecadmin.pojo.Cart;
import com.bistu.ecadmin.pojo.CartVO;
import com.bistu.ecadmin.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceIml implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Override
    @Transactional
    public void addToCart(int userId, int skuId) {
        // 先查询 sku 获取 spu_id
        Long spuId = cartMapper.getSpuIdBySkuId(skuId);

        if (spuId == null) {
            throw new RuntimeException("商品规格不存在");
        }

        // 查询商品状态
        Integer status = cartMapper.getSpuStatusBySkuId(skuId);

        // 如果商品下架（status = 0），不允许加入购物车
        if (status == null || status == 0) {
            throw new RuntimeException("商品已下架，无法加入购物车");
        }

        // 添加购物车
        cartMapper.insertOrTouch(userId, skuId);

        // 如果成功添加（包括更新），则增加购物车计数
        cartMapper.incrementCartAddCount(spuId);
    }


    @Override
    public List<Cart> listByUser(int userId) {
        return cartMapper.selectByUserId(userId);
    }

    @Override
    @Transactional
    public boolean deleteItem(int userId, int skuId) {
        return cartMapper.deleteByUserAndSku(userId, skuId) > 0;
    }

    @Override
    public List<Cart> listByUserPaged(int userId, int offset, int pageSize) {
        return cartMapper.selectByUserIdPaged(userId, offset, pageSize);
    }

    @Override
    public int countByUser(int userId) {
        return cartMapper.countByUserId(userId);
    }

    @Override
    public List<Cart> listAllPaged(int offset, int pageSize) {
        return cartMapper.selectAllPaged(offset, pageSize);
    }

    @Override
    public int countAll() {
        return cartMapper.countAll();
    }

    // 新增方法：返回包含商品信息的购物车列表
    @Override
    public List<CartVO> listByUserWithProduct(int userId) {
        return cartMapper.selectByUserIdWithProduct(userId);
    }

    @Override
    public List<CartVO> listByUserPagedWithProduct(int userId, int offset, int pageSize) {
        return cartMapper.selectByUserIdPagedWithProduct(userId, offset, pageSize);
    }

    @Override
    public List<CartVO> listAllPagedWithProduct(int offset, int pageSize) {
        return cartMapper.selectAllPagedWithProduct(offset, pageSize);
    }
}
