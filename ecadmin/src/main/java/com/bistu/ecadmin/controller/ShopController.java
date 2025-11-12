package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.Shop;
import com.bistu.ecadmin.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺管理控制器
 */
@RestController
@RequestMapping("/admin/ecadmin/shop")
@Slf4j
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 新增店铺
     */
    @PostMapping("/add")
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
    public Result<Shop> getShopById(@PathVariable Long id) {
        try {
            return shopService.getShopById(id);
        } catch (Exception e) {
            log.error("查询店铺详情失败", e);
            return Result.error("查询店铺详情失败：" + e.getMessage());
        }
    }
}
