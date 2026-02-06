package com.bistu.ecadmin.controller;
import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.Shop;
import com.bistu.ecadmin.service.ShopService;
import com.bistu.ecadmin.annotation.OperateLog;
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
    
    @Autowired
    private com.bistu.ecadmin.dao.mapper.ShopMapper shopMapper;

    /**
     * 新增店铺
     */
    @RequiresPermissions("shop:add")
    @PostMapping("/add")
    @ApiOperation("新增店铺")
    @OperateLog(operation = "新增农产品店铺")
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
    @RequiresPermissions("shop:update")
    @PostMapping("/update")
    @ApiOperation("更新店铺")
    @OperateLog(operation = "更新农产品店铺")
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
    @RequiresPermissions("shop:delete")
    @DeleteMapping("/{id}")
    @ApiOperation("删除店铺")
    @ApiImplicitParam(name = "id", value = "店铺ID", required = true, dataType = "Long", paramType = "path")
    @OperateLog(operation = "删除农产品店铺")
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
    @RequiresPermissions("shop:list")
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
    @RequiresPermissions("shop:list")
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

    /**
     * 获取店铺选项列表（用于下拉选择）
     * 根据用户ID和角色判断：管理员可查看全部店铺，普通用户只能查看自己关联的店铺
     */
    @GetMapping("/options")
    @ApiOperation("获取店铺下拉选项")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "query"),
        @ApiImplicitParam(name = "roleIds", value = "角色ID列表（逗号分隔）", dataType = "String", paramType = "query")
    })
    public Result<List<Shop>> listShopOptions(@RequestParam Long userId,
                                              @RequestParam(required = false) String roleIds) {
        try {
            // 判断是否是管理员：userId=10011 或 roleIds 包含 1
            boolean isAdmin = false;
            if (userId != null && userId == 10011) {
                isAdmin = true;
            } else if (roleIds != null && !roleIds.isEmpty()) {
                String[] roleIdArray = roleIds.split(",");
                for (String roleId : roleIdArray) {
                    if ("1".equals(roleId.trim())) {
                        isAdmin = true;
                        break;
                    }
                }
            }
            
            List<Shop> shops = shopMapper.listShopOptions(userId, isAdmin);
            log.info("获取店铺下拉列表（用户ID：{}，是否管理员：{}），数量：{}", userId, isAdmin, shops.size());
            return Result.success(shops);
        } catch (Exception e) {
            log.error("获取店铺下拉列表失败", e);
            return Result.error("获取店铺列表失败：" + e.getMessage());
        }
    }
}