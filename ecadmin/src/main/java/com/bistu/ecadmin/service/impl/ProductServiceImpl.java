package com.bistu.ecadmin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.util.CommonUtil;
import com.bistu.common.util.constants.ErrorEnum;
import com.bistu.ecadmin.dao.ProductDao;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.service.ProductService;
import com.bistu.ecadmin.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public JSONObject createProduct(JSONObject jsonObject) {
        try {
            // 新增商品
            int result = productDao.addProduct(jsonObject);

            // 获取生成的商品ID
            Integer productId = jsonObject.getInteger("id");

            if (productId != null && productId > 0) {
                // 处理预览图
                if (jsonObject.containsKey("previewImages")) {
                    List<String> previewImages = jsonObject.getJSONArray("previewImages").toJavaList(String.class);
                    List<JSONObject> previewImageList = new ArrayList<>();
                    for (int i = 0; i < previewImages.size(); i++) {
                        JSONObject previewImage = new JSONObject();
                        previewImage.put("spu_id", productId);
                        previewImage.put("sort", i);
                        // 修改这里：确保存储的是完整URL
                        String imageUrl = previewImages.get(i);
                        if (!imageUrl.startsWith("/uploads/")) {
                            // 如果不是完整路径，补充访问路径前缀
                            imageUrl = "/uploads/" + imageUrl;
                        }
                        previewImage.put("img_url", imageUrl);
                        previewImageList.add(previewImage);
                    }
                    if (!previewImageList.isEmpty()) {
                        productDao.batchAddPreviewImages(previewImageList);
                    }
                }

                // 处理详情图
                if (jsonObject.containsKey("detailImages")) {
                    List<String> detailImages = jsonObject.getJSONArray("detailImages").toJavaList(String.class);
                    List<JSONObject> detailImageList = new ArrayList<>();
                    for (int i = 0; i < detailImages.size(); i++) {
                        JSONObject detailImage = new JSONObject();
                        detailImage.put("spu_id", productId);
                        detailImage.put("type", 1); // 图片类型
                        detailImage.put("sort", i);
                        // 修改这里：确保存储的是完整URL
                        String imageUrl = detailImages.get(i);
                        if (!imageUrl.startsWith("/uploads/")) {
                            // 如果不是完整路径，补充访问路径前缀
                            imageUrl = "/uploads/" + imageUrl;
                        }
                        detailImage.put("content", imageUrl);
                        detailImageList.add(detailImage);
                    }
                    if (!detailImageList.isEmpty()) {
                        productDao.batchAddDetailImages(detailImageList);
                    }
                }

                // 处理规格数组
                if (jsonObject.containsKey("specifications")) {
                    List<JSONObject> specifications = jsonObject.getJSONArray("specifications").toJavaList(JSONObject.class);
                    List<JSONObject> specificationList = new ArrayList<>();

                    for (JSONObject spec : specifications) {
                        JSONObject specification = new JSONObject();
                        specification.put("spu_id", productId);
                        specification.put("specs", spec.getString("specName"));
                        specification.put("price", spec.getInteger("price"));
                        specification.put("stock", spec.getInteger("stock"));
                        specificationList.add(specification);
                    }

                    if (!specificationList.isEmpty()) {
                        productDao.batchAddSpecifications(specificationList);
                    }
                }

                return CommonUtil.successJson();
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品添加失败", new JSONObject());
            }
        } catch (Exception e) {
            log.error("添加商品失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    public JSONObject updateProduct(JSONObject jsonObject) {
        try {
            // 检查商品是否存在
            Long productId = jsonObject.getLong("id");
            if (productId == null || productId <= 0) {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品ID不能为空", new JSONObject());
            }

            JSONObject existingProduct = productDao.getProductById(productId, null);
            if (existingProduct == null) {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品不存在", new JSONObject());
            }

            // 更新商品基本信息
            int result = productDao.updateProduct(jsonObject);

            if (result > 0) {
                // 先删除原有的关联数据
                productDao.deletePreviewImagesByProductId(productId);
                productDao.deleteDetailImagesByProductId(productId);
                productDao.deleteSpecificationsByProductId(productId);

                // 重新添加预览图
                if (jsonObject.containsKey("previewImages")) {
                    List<String> previewImages = jsonObject.getJSONArray("previewImages").toJavaList(String.class);
                    List<JSONObject> previewImageList = new ArrayList<>();
                    for (int i = 0; i < previewImages.size(); i++) {
                        JSONObject previewImage = new JSONObject();
                        previewImage.put("spu_id", productId);
                        previewImage.put("sort", i);
                        // 确保存储的是完整URL
                        String imageUrl = previewImages.get(i);
                        if (!imageUrl.startsWith("/uploads/")) {
                            // 如果不是完整路径，补充访问路径前缀
                            imageUrl = "/uploads/" + imageUrl;
                        }
                        previewImage.put("img_url", imageUrl);
                        previewImageList.add(previewImage);
                    }
                    if (!previewImageList.isEmpty()) {
                        productDao.batchAddPreviewImages(previewImageList);
                    }
                }

                // 重新添加详情图
                if (jsonObject.containsKey("detailImages")) {
                    List<String> detailImages = jsonObject.getJSONArray("detailImages").toJavaList(String.class);
                    List<JSONObject> detailImageList = new ArrayList<>();
                    for (int i = 0; i < detailImages.size(); i++) {
                        JSONObject detailImage = new JSONObject();
                        detailImage.put("spu_id", productId);
                        detailImage.put("type", 1); // 图片类型
                        detailImage.put("sort", i);
                        // 确保存储的是完整URL
                        String imageUrl = detailImages.get(i);
                        if (!imageUrl.startsWith("/uploads/")) {
                            // 如果不是完整路径，补充访问路径前缀
                            imageUrl = "/uploads/" + imageUrl;
                        }
                        detailImage.put("content", imageUrl);
                        detailImageList.add(detailImage);
                    }
                    if (!detailImageList.isEmpty()) {
                        productDao.batchAddDetailImages(detailImageList);
                    }
                }

                // 重新添加规格数组
                if (jsonObject.containsKey("specifications")) {
                    List<JSONObject> specifications = jsonObject.getJSONArray("specifications").toJavaList(JSONObject.class);
                    List<JSONObject> specificationList = new ArrayList<>();

                    for (JSONObject spec : specifications) {
                        JSONObject specification = new JSONObject();
                        specification.put("spu_id", productId);
                        specification.put("specs", spec.getString("specName"));
                        specification.put("price", spec.getInteger("price"));
                        specification.put("stock", spec.getInteger("stock"));
                        specificationList.add(specification);
                    }

                    if (!specificationList.isEmpty()) {
                        productDao.batchAddSpecifications(specificationList);
                    }
                }

                return CommonUtil.successJson();
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品更新失败", new JSONObject());
            }
        } catch (Exception e) {
            log.error("更新商品失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    public JSONObject deleteProduct(Long productId) {
        try {
            // 检查商品是否存在
            if (productId == null || productId <= 0) {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品ID不能为空", new JSONObject());
            }

            JSONObject existingProduct = productDao.getProductById(productId, null);
            if (existingProduct == null) {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品不存在", new JSONObject());
            }

            // 先删除关联数据（预览图、详情图、规格）
            productDao.deletePreviewImagesByProductId(productId);
            productDao.deleteDetailImagesByProductId(productId);
            productDao.deleteSpecificationsByProductId(productId);

            // 删除商品主记录
            int result = productDao.deleteProductById(productId);

            if (result > 0) {
                return CommonUtil.successJson();
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品删除失败", new JSONObject());
            }
        } catch (Exception e) {
            log.error("删除商品失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    public PageResult listProducts(JSONObject params) {
        try {
            // 从UserContext获取当前用户ID（用于判断是否收藏）
            Long currentUserId = UserContext.getUserId();
            params.put("currentUserId", currentUserId);
            
            // 获取分页参数
            int pageNum = params.getIntValue("pageNum");
            int pageRow = params.getIntValue("pageRow");

            // 计算偏移量
            int offset = (pageNum - 1) * pageRow;
            params.put("offset", offset);
            params.put("limit", pageRow);

            // 查询商品列表
            List<JSONObject> products = productDao.listProducts(params);

            // 查询总数
            int totalCount = productDao.countProducts(params);

            // 为每个商品添加关联信息（预览图、详情图、规格）
            for (JSONObject product : products) {
                Long productId = product.getLong("id");

                // 获取预览图
                List<String> previewImages = productDao.getPreviewImagesByProductId(productId);
                // 确保返回的预览图URL是完整的
                for (int i = 0; i < previewImages.size(); i++) {
                    String imgUrl = previewImages.get(i);
                    if (!imgUrl.startsWith("http") && !imgUrl.startsWith("/uploads/")) {
                        previewImages.set(i, "/uploads/" + imgUrl);
                    }
                }
                product.put("previewImages", previewImages);

                List<String> detailImages = productDao.getDetailImagesByProductId(productId);
                // 确保返回的详情图URL是完整的
                for (int i = 0; i < detailImages.size(); i++) {
                    String imgUrl = detailImages.get(i);
                    if (!imgUrl.startsWith("http") && !imgUrl.startsWith("/uploads/")) {
                        detailImages.set(i, "/uploads/" + imgUrl);
                    }
                }
                product.put("detailImages", detailImages);

                // 获取规格
                List<JSONObject> specifications = productDao.getSpecificationsByProductId(productId);
                product.put("specifications", specifications);
            }

            return new PageResult(totalCount, products);
        } catch (Exception e) {
            log.error("查询商品列表失败", e);
            return new PageResult(0, new ArrayList<>());
        }
    }

    @Override
    public JSONObject incrementViewCount(Long productId) {
        try {
            // 检查商品ID是否有效
            if (productId == null || productId <= 0) {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品ID不能为空", new JSONObject());
            }

            // 检查商品是否存在
            JSONObject existingProduct = productDao.getProductById(productId, null);
            if (existingProduct == null) {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品不存在", new JSONObject());
            }

            // 增加浏览次数
            int result = productDao.incrementViewCount(productId);

            if (result > 0) {
                return CommonUtil.successJson();
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_400, "增加浏览次数失败", new JSONObject());
            }
        } catch (Exception e) {
            log.error("增加商品浏览次数失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    public JSONObject getProductById(Long productId, Long currentUserId) {
        try {
            // 如果参数中没有currentUserId，则从UserContext获取
            if (currentUserId == null) {
                currentUserId = UserContext.getUserId();
            }
            
            // 检查商品ID是否有效
            if (productId == null || productId <= 0) {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品ID不能为空", new JSONObject());
            }

            // 查询商品基本信息
            JSONObject product = productDao.getProductById(productId, currentUserId);
            if (product == null) {
                return CommonUtil.errorJson(ErrorEnum.E_400, "商品不存在", new JSONObject());
            }

            // 处理shopAvatar字段，如果存在且不以http或/uploads/开头，则添加/uploads/前缀
            if (product.containsKey("shopAvatar") && product.getString("shopAvatar") != null) {
                String shopAvatar = product.getString("shopAvatar");
                if (!shopAvatar.startsWith("http") && !shopAvatar.startsWith("/uploads/")) {
                    product.put("shopAvatar", "/uploads/" + shopAvatar);
                }
            }

            // 获取预览图
            List<String> previewImages = productDao.getPreviewImagesByProductId(productId);
            // 确保返回的预览图URL是完整的
            for (int i = 0; i < previewImages.size(); i++) {
                String imgUrl = previewImages.get(i);
                if (!imgUrl.startsWith("http") && !imgUrl.startsWith("/uploads/")) {
                    previewImages.set(i, "/uploads/" + imgUrl);
                }
            }
            product.put("previewImages", previewImages);

            // 获取详情图
            List<String> detailImages = productDao.getDetailImagesByProductId(productId);
            // 确保返回的详情图URL是完整的
            for (int i = 0; i < detailImages.size(); i++) {
                String imgUrl = detailImages.get(i);
                if (!imgUrl.startsWith("http") && !imgUrl.startsWith("/uploads/")) {
                    detailImages.set(i, "/uploads/" + imgUrl);
                }
            }
            product.put("detailImages", detailImages);

            // 获取规格
            List<JSONObject> specifications = productDao.getSpecificationsByProductId(productId);
            product.put("specifications", specifications);

            return CommonUtil.successJson(product);
        } catch (Exception e) {
            log.error("查询商品详情失败", e);
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }
}
