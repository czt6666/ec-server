package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.mapper.CartMapper;
import com.bistu.ecadmin.pojo.Cart;
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
        cartMapper.insertOrTouch(userId, skuId);
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
    // CartServiceImpl.java
    @Override
    public List<Cart> listAllPaged(int offset, int pageSize) {
        return cartMapper.selectAllPaged(offset, pageSize);
    }

    @Override
    public int countAll() {
        return cartMapper.countAll();
    }
}
