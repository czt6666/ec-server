package com.bistu.ecadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.util.CommonUtil;
import com.bistu.common.util.constants.ErrorEnum;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductsController {

    @Autowired
    private ProductService productService;

    /**
     * 添加商品
     */
    @PostMapping(value = "/subject/add", consumes = {"application/json", "application/x-www-form-urlencoded"})
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
            
            // 添加userId参数
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
}