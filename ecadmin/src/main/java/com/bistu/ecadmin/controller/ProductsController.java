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
                                   @RequestParam(required = false) Integer status) {
        try {
            JSONObject params = new JSONObject();
            params.put("pageNum", pageNum);
            params.put("pageRow", pageRow);
            params.put("title", title);
            params.put("status", status);
            
            PageResult result = productService.listProducts(params);
            return CommonUtil.successJson(result);
        } catch (Exception e) {
            log.error("查询商品列表失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }
    
    /**
     * 根据ID查询单个商品
     */
    @GetMapping("/subject/detail")
    public JSONObject getProductById(@RequestParam Long id) {
        try {
            // 验证必填参数
            if (id == null || id <= 0) {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品ID不能为空");
            }
            
            return productService.getProductById(id);
        } catch (Exception e) {
            log.error("查询商品详情失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }
}