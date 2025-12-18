package com.bistu.ecadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.util.CommonUtil;
import com.bistu.common.util.constants.ErrorEnum;
import com.bistu.ecadmin.service.ProductService;
import com.bistu.ecadmin.pojo.PageResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/products")
@Api(tags = "商品管理接口")
@Slf4j
public class ProductsController {

    @Autowired
    private ProductService productService;

    /**
     * 添加商品
     */
    @PostMapping(value = "/subject/add", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @ApiOperation(value = "添加商品", notes = "添加新的商品信息")
    public JSONObject createProduct(@RequestBody(required = false) JSONObject requestJson, HttpServletRequest request) {
        // 如果没有JSON请求体，则从请求参数中获取
        if (requestJson == null) {
            requestJson = CommonUtil.request2Json(request);
        }
        
        // 验证必填参数
        CommonUtil.hasAllRequired(requestJson, "title, description, productUrl, previewImages, detailImages, status, userId");
        
        return productService.createProduct(requestJson);
    }
    
    /**
     * 更新商品
     */
    @PostMapping(value = "/subject/update", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @ApiOperation(value = "更新商品", notes = "更新商品信息")
    public JSONObject updateProduct(@RequestBody(required = false) JSONObject requestJson, HttpServletRequest request) {
        // 如果没有JSON请求体，则从请求参数中获取
        if (requestJson == null) {
            requestJson = CommonUtil.request2Json(request);
        }
        
        // 验证必填参数
        CommonUtil.hasAllRequired(requestJson, "id, title, description, productUrl, previewImages, detailImages, status");
        
        return productService.updateProduct(requestJson);
    }
    
    /**
     * 删除商品
     */
    @PostMapping("/subject/delete")
    @ApiOperation(value = "删除商品", notes = "根据ID删除商品")
    public JSONObject deleteProduct(@RequestBody JSONObject requestJson) {
        try {
            // 验证必填参数
            CommonUtil.hasAllRequired(requestJson, "id");
            
            Long productId = requestJson.getLong("id");
            return productService.deleteProduct(productId);
        } catch (Exception e) {
            log.error("删除商品失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }
    
    /**
     * 查询商品列表
     */
    @GetMapping("/subject/list")
    @ApiOperation(value = "获取商品列表", notes = "分页获取商品列表信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "pageRow", value = "每页数量", defaultValue = "10", dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "title", value = "商品标题", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "status", value = "商品状态", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", paramType = "query"),
        @ApiImplicitParam(name = "shopName", value = "店铺名称", dataType = "String", paramType = "query")
    })
    public JSONObject listProducts(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(required = false, defaultValue = "10") Integer pageRow,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) Integer status,
                                   @RequestParam(required = false) Long userId,
                                   @RequestParam(required = false) String shopName) {
        try {
            JSONObject params = new JSONObject();
            params.put("pageNum", pageNum);
            params.put("pageRow", pageRow);
            params.put("title", title);
            params.put("status", status);
            
            // 添加userId参数（商品所属用户ID）
            if (userId != null) {
                params.put("userId", userId);
            }
            
            // 添加shopName参数
            if (shopName != null && !shopName.isEmpty()) {
                params.put("shopName", shopName);
            }
            
            PageResult result = productService.listProducts(params);
            return CommonUtil.successJson(result);
        } catch (Exception e) {
            log.error("查询商品列表失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }
    
    /**
     * 增加商品浏览次数
     */
    @PostMapping("/subject/view")
    @ApiOperation(value = "增加商品浏览次数", notes = "根据ID增加商品浏览次数")
    public JSONObject incrementViewCount(@RequestBody JSONObject requestJson) {
        try {
            // 验证必填参数
            CommonUtil.hasAllRequired(requestJson, "id");
            
            Long productId = requestJson.getLong("id");
            return productService.incrementViewCount(productId);
        } catch (Exception e) {
            log.error("增加商品浏览次数失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }
    
    /**
     * 根据商品ID查询商品详情
     */
    @GetMapping("/subject/detail")
    @ApiOperation(value = "获取商品详情", notes = "根据商品ID获取商品详细信息")
    @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "Long", paramType = "query")
    public JSONObject getProductById(@RequestParam Long id) {
        try {
            return productService.getProductById(id, null);
        } catch (Exception e) {
            log.error("查询商品详情失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

}