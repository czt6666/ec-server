package com.bistu.ecadmin.service;


import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.Shop;

import java.util.List;

/**
 * 店铺服务接口
 */
public interface ShopService {

    /**
     * 新增店铺（同时创建商家账号）
     * @param shop 店铺信息
     * @return 操作结果
     */
    Result<Shop> createShop(Shop shop);

    /**
     * 更新店铺
     * @param shop 店铺信息
     * @return 操作结果
     */
    Result<Shop> updateShop(Shop shop);

    /**
     * 删除店铺
     * @param shopId 店铺ID
     * @return 操作结果
     */
    Result<String> deleteShop(Long shopId);

    /**
     * 查询店铺列表（分页）
     * @param shopName 店铺名称（模糊查询）
     * @param productType 产品类型
     * @param businessStatus 经营状态
     * @param village 所属村（模糊查询）
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Result<PageResult> listShops(String shopName, String productType,
                                                       Integer businessStatus, String village,
                                                       Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询店铺详情
     * @param shopId 店铺ID
     * @return 店铺信息
     */
    Result<Shop> getShopById(Long shopId);
    Result<List<JSONObject>> listProductsByShop(Long shopId, Integer status);
}
