package com.bistu.ecadmin.pojo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.alibaba.fastjson.JSONObject;

/**
 * 购物车视图对象，包含商品信息
 */
@Data
public class CartVO {
    // 购物车基本信息
    private Long id;
    private Long userId;
    private Long skuId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // SKU信息
    private Long spuId;
    private String specs;  // 规格信息（JSON字符串）
    private Integer price; // 价格（分）
    private Integer stock; // 库存

    // SPU信息
    private String title;  // 商品标题
    private String intro;  // 商品简介
    private Integer status; // 商品状态 0下架 1上架

    // 商品预览图（第一张）- 用于兼容旧字段/列表缩略图
    private String previewImage;

    // 完整的商品信息（用于小程序端跳转购买）
    private List<String> previewImages;  // 预览图列表
    private List<String> detailImages;  // 详情图列表
    private List<JSONObject> specifications;  // 规格列表
}
