package com.bistu.ecadmin.service;

import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.pojo.PageResult;

public interface ProductService {
    /**
     * 新增商品
     */
    JSONObject createProduct(JSONObject jsonObject);
    
    /**
     * 更新商品
     */
    JSONObject updateProduct(JSONObject jsonObject);
    
    /**
     * 删除商品
     */
    JSONObject deleteProduct(Long productId);
    
    /**
     * 查询商品列表
     */
    PageResult listProducts(JSONObject params);
    
    /**
     * 增加商品浏览次数
     */
    JSONObject incrementViewCount(Long productId);
    
    /**
     * 根据商品ID查询商品详情
     */
    JSONObject getProductById(Long productId);
}