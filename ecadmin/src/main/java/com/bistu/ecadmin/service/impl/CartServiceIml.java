package com.bistu.ecadmin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.dao.ProductDao;
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
    
    @Autowired
    private ProductDao productDao;

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
        List<CartVO> cartList = cartMapper.selectByUserIdWithProduct(userId);
        enrichCartListWithProductInfo(cartList);
        return cartList;
    }

    @Override
    public List<CartVO> listByUserPagedWithProduct(int userId, int offset, int pageSize) {
        List<CartVO> cartList = cartMapper.selectByUserIdPagedWithProduct(userId, offset, pageSize);
        enrichCartListWithProductInfo(cartList);
        return cartList;
    }

    @Override
    public List<CartVO> listAllPagedWithProduct(int offset, int pageSize) {
        List<CartVO> cartList = cartMapper.selectAllPagedWithProduct(offset, pageSize);
        enrichCartListWithProductInfo(cartList);
        return cartList;
    }
    
    /**
     * 为购物车列表补充完整的商品信息（预览图、详情图、规格）
     */
    private void enrichCartListWithProductInfo(List<CartVO> cartList) {
        for (CartVO cart : cartList) {
            if (cart.getSpuId() != null) {
                Long spuId = cart.getSpuId();
                
                // 获取预览图列表
                List<String> previewImages = productDao.getPreviewImagesByProductId(spuId);
                // 确保返回的预览图URL是完整的
                for (int i = 0; i < previewImages.size(); i++) {
                    String imgUrl = previewImages.get(i);
                    if (!imgUrl.startsWith("http") && !imgUrl.startsWith("/uploads/")) {
                        previewImages.set(i, "/uploads/" + imgUrl);
                    }
                }
                cart.setPreviewImages(previewImages);
                
                // 获取详情图列表
                List<String> detailImages = productDao.getDetailImagesByProductId(spuId);
                // 确保返回的详情图URL是完整的
                for (int i = 0; i < detailImages.size(); i++) {
                    String imgUrl = detailImages.get(i);
                    if (!imgUrl.startsWith("http") && !imgUrl.startsWith("/uploads/")) {
                        detailImages.set(i, "/uploads/" + imgUrl);
                    }
                }
                cart.setDetailImages(detailImages);
                
                // 获取规格列表
                List<JSONObject> specifications = productDao.getSpecificationsByProductId(spuId);
                cart.setSpecifications(specifications);
            }
        }
    }
}
