package com.bistu.ecadmin.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Products
 * @description: 商品相关dao
 * @date: 2025/11
 */
public interface ProductDao {
    /**
     * 新增商品
     */
    int addProduct(JSONObject jsonObject);

    /**
     * 更新商品
     */
    int updateProduct(JSONObject jsonObject);

    /**
     * 删除商品
     */
    int deleteProductById(Long productId);

    /**
     * 增加商品浏览次数
     */
    int incrementViewCount(Long productId);

    /**
     * 批量新增预览图
     */
    int batchAddPreviewImages(List<JSONObject> previewImages);

    /**
     * 批量新增详情图
     */
    int batchAddDetailImages(List<JSONObject> detailImages);

    /**
     * 批量新增规格
     */
    int batchAddSpecifications(List<JSONObject> specifications);

    /**
     * 删除商品的所有预览图
     */
    int deletePreviewImagesByProductId(Long productId);

    /**
     * 删除商品的所有详情图
     */
    int deleteDetailImagesByProductId(Long productId);

    /**
     * 删除商品的所有规格
     */
    int deleteSpecificationsByProductId(Long productId);

    /**
     * 查询商品列表
     */
    List<JSONObject> listProducts(JSONObject params);

    /**
     * 查询商品总数
     */
    int countProducts(JSONObject params);

    /**
     * 根据商品ID查询预览图
     */
    List<String> getPreviewImagesByProductId(Long productId);

    /**
     * 根据商品ID查询详情图
     */
    List<String> getDetailImagesByProductId(Long productId);

    /**
     * 根据商品ID查询规格
     */
    List<JSONObject> getSpecificationsByProductId(Long productId);

    /**
     * 根据商品ID查询商品基本信息
     */
    JSONObject getProductById(@Param("productId") Long productId, @Param("userId") Long userId);
}
