package com.bistu.ecadmin.controller;
import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.Shop;
import com.bistu.ecadmin.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺管理控制器
 */
@RestController
@RequestMapping("/admin/ecadmin/shop")
@Slf4j
@Api(tags = "店铺管理")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 新增店铺
     */
    @PostMapping("/add")
    @ApiOperation("新增店铺")
    public Result<Shop> createShop(@RequestBody Shop shop) {
        try {
            return shopService.createShop(shop);
        } catch (Exception e) {
            log.error("新增店铺失败", e);
            return Result.error("新增店铺失败：" + e.getMessage());
        }
    }

    /**
     * 更新店铺
     */
    @PostMapping("/update")
    @ApiOperation("更新店铺")
    public Result<Shop> updateShop(@RequestBody Shop shop) {
        try {
            return shopService.updateShop(shop);
        } catch (Exception e) {
            log.error("更新店铺失败", e);
            return Result.error("更新店铺失败：" + e.getMessage());
        }
    }

    /**
     * 删除店铺
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除店铺")
    @ApiImplicitParam(name = "id", value = "店铺ID", required = true, dataType = "Long", paramType = "path")
    public Result<String> deleteShop(@PathVariable Long id) {
        try {
            return shopService.deleteShop(id);
        } catch (Exception e) {
            log.error("删除店铺失败", e);
            return Result.error("删除店铺失败：" + e.getMessage());
        }
    }

    /**
     * 查询店铺列表（分页）
     */
    @GetMapping("/list")
    @ApiOperation("查询店铺列表（分页）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "shopName", value = "店铺名称", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "productType", value = "商品类型", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "businessStatus", value = "营业状态", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "village", value = "所属村庄", dataType = "String", paramType = "query")
    })
    public Result<PageResult> listShops(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String shopName,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) Integer businessStatus,
            @RequestParam(required = false) String village) {
        try {
            return shopService.listShops(shopName, productType, businessStatus, village, pageNum, pageSize);
        } catch (Exception e) {
            log.error("查询店铺列表失败", e);
            return Result.error("查询店铺列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询店铺详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询店铺详情")
    @ApiImplicitParam(name = "id", value = "店铺ID", required = true, dataType = "Long", paramType = "path")
    public Result<Shop> getShopById(@PathVariable Long id) {
        try {
            return shopService.getShopById(id);
        } catch (Exception e) {
            log.error("查询店铺详情失败", e);
            return Result.error("查询店铺详情失败：" + e.getMessage());
        }
    }
    @GetMapping("/{shopId}/products")
    @ApiOperation("获取商家下的所有商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "商品状态（可选）", dataType = "Integer", paramType = "query")
    })
    public Result<List<JSONObject>> listProductsByShop(@PathVariable Long shopId,
                                                       @RequestParam(required = false) Integer status) {
        try {
            return shopService.listProductsByShop(shopId, status);
        } catch (Exception e) {
            log.error("查询商家商品失败", e);
            return Result.error("查询商家商品失败：" + e.getMessage());
        }
    }
}
