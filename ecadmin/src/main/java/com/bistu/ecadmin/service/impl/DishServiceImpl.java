package com.bistu.ecadmin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.dao.mapper.DishMapper;
import com.bistu.ecadmin.dao.mapper.UserRoleMapper;
import com.bistu.ecadmin.dao.mapper.DishCategoryMapper;
import com.bistu.ecadmin.pojo.Dish;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.DishService;
import com.bistu.ecadmin.dao.DTO.DishDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜品Service实现类
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    
    @Autowired
    private DishMapper dishMapper;
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Autowired
    private DishCategoryMapper dishCategoryMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> addDish(JSONObject params) {
        try {
            // 创建菜品对象
            Dish dish = new Dish();
            
            // 设置参数
            dish.setRestaurantId(params.getLong("restaurantId"));
            dish.setCategoryId(params.getLong("categoryId"));
            dish.setDishName(params.getString("dishName"));
            dish.setCoverImgUrl(params.getString("coverImgUrl")); // 添加coverImgUrl字段处理
            
            // 价格处理
            BigDecimal price = params.getBigDecimal("price");
            if (price == null) {
                return Result.error("菜品价格不能为空");
            }
            dish.setPrice(price);
            
            dish.setUnit(params.getString("unit"));
            
            // 状态处理，默认为1（上架）
            Integer dishStatus = params.getInteger("status");
            dish.setDishStatus(dishStatus != null ? dishStatus : 1);
            
            dish.setSummary(params.getString("description"));
            
            // 参数验证
            if (dish.getRestaurantId() == null) {
                return Result.error("餐厅ID不能为空");
            }
            
            if (dish.getCategoryId() == null) {
                return Result.error("菜品分类ID不能为空");
            }
            
            if (dish.getDishName() == null || dish.getDishName().isEmpty()) {
                return Result.error("菜品名称不能为空");
            }
            
            // 调用Mapper插入数据
            int rows = dishMapper.addDish(dish);
            
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("新增菜品失败");
            }
        } catch (Exception e) {
            log.error("新增菜品失败", e);
            return Result.error("新增菜品失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<?> listDishes(JSONObject params) {
        try {
            // 获取分页参数
            int page = params.getIntValue("page");
            int pageSize = params.getIntValue("pageSize");
            
            // 参数校验
            if (page <= 0) {
                page = 1;
            }
            if (pageSize <= 0 || pageSize > 100) {
                pageSize = 10;
            }
            
            // 计算偏移量
            int offset = (page - 1) * pageSize;
            
            // 获取查询条件
            String restaurantName = params.getString("restaurantName");
            String categoryName = params.getString("categoryName");
            String dishName = params.getString("dishName");
            Integer status = params.getInteger("status");
            Long userId = params.getLong("userId"); // 获取用户ID参数
            
            // 权限控制逻辑
            Long restaurantId = null;
            boolean isAdmin = false;
            
            // 如果提供了userId，则进行权限控制
            if (userId != null) {
                // 查询用户的角色ID列表
                List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(userId);
                
                // 检查用户是否具有管理员角色（role_id=1）
                if (roleIds.contains(1L)) {
                    isAdmin = true; // 管理员可以查看所有菜品
                } else {
                    // 非管理员用户只能查看自己关联餐厅的菜品
                    restaurantId = dishCategoryMapper.getRestaurantIdByUserId(userId);
                    if (restaurantId == null) {
                        return Result.error("未找到用户关联的餐厅");
                    }
                }
            }
            
            // 查询总数
            int total;
            List<DishDTO> dishes;
            
            if (isAdmin) {
                // 管理员查询所有菜品
                total = dishMapper.countDishes(restaurantName, categoryName, dishName, status);
                dishes = dishMapper.listDishes(offset, pageSize, restaurantName, categoryName, dishName, status);
            } else {
                // 非管理员根据餐厅ID查询菜品
                // 这里我们需要修改DishMapper的方法来支持通过restaurantId过滤
                total = dishMapper.countDishesByRestaurantId(restaurantId, categoryName, dishName, status);
                dishes = dishMapper.listDishesByRestaurantId(offset, pageSize, restaurantId, categoryName, dishName, status);
            }
            
            // 构造返回结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("records", dishes);
            resultData.put("total", total);
            resultData.put("page", page);
            resultData.put("pageSize", pageSize);
            resultData.put("pages", (total + pageSize - 1) / pageSize); // 计算总页数
            
            return Result.success(resultData);
        } catch (Exception e) {
            log.error("查询菜品列表失败", e);
            return Result.error("查询菜品列表失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<?> getDishDetail(JSONObject params) {
        try {
            // 获取参数
            Long id = params.getLong("id");
            Long userId = params.getLong("userId");
            
            // 参数验证
            if (id == null) {
                return Result.error("菜品ID不能为空");
            }
            
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            
            // 查询菜品详情
            Dish dish = dishMapper.getDishById(id);
            
            if (dish == null) {
                return Result.error("菜品不存在");
            }
            
            // 构造返回数据，确保包含前端需要的所有字段
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("id", dish.getId());
            resultData.put("restaurantId", dish.getRestaurantId());
            resultData.put("categoryId", dish.getCategoryId());
            resultData.put("dishName", dish.getDishName() != null ? dish.getDishName() : "");
            resultData.put("price", dish.getPrice() != null ? dish.getPrice() : 0);
            resultData.put("unit", dish.getUnit() != null ? dish.getUnit() : "");
            resultData.put("status", dish.getDishStatus() != null ? dish.getDishStatus() : 1);
            resultData.put("description", dish.getSummary() != null ? dish.getSummary() : "");
            resultData.put("coverImgUrl", dish.getCoverImgUrl() != null ? dish.getCoverImgUrl() : ""); // 添加coverImgUrl字段返回
            
            // 返回结果
            return Result.success(resultData);
        } catch (Exception e) {
            log.error("获取菜品详情失败", e);
            return Result.error("获取菜品详情失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateDish(JSONObject params) {
        try {
            // 获取参数
            Long id = params.getLong("id");
            Long userId = params.getLong("userId");
            
            // 参数验证
            if (id == null) {
                return Result.error("菜品ID不能为空");
            }
            
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            
            // 检查菜品是否存在
            Dish existingDish = dishMapper.getDishById(id);
            if (existingDish == null) {
                return Result.error("菜品不存在");
            }
            
            // 创建菜品对象并设置参数
            Dish dish = new Dish();
            dish.setId(id);
            dish.setRestaurantId(params.getLong("restaurantId"));
            dish.setCategoryId(params.getLong("categoryId"));
            dish.setDishName(params.getString("dishName"));
            dish.setCoverImgUrl(params.getString("coverImgUrl")); // 添加coverImgUrl字段处理
            
            // 价格处理
            BigDecimal price = params.getBigDecimal("price");
            if (price == null) {
                return Result.error("菜品价格不能为空");
            }
            dish.setPrice(price);
            
            dish.setUnit(params.getString("unit"));
            
            // 状态处理
            Integer dishStatus = params.getInteger("status");
            if (dishStatus != null) {
                dish.setDishStatus(dishStatus);
            }
            
            dish.setSummary(params.getString("description"));
            
            // 参数验证
            if (dish.getRestaurantId() == null) {
                return Result.error("餐厅ID不能为空");
            }
            
            if (dish.getCategoryId() == null) {
                return Result.error("菜品分类ID不能为空");
            }
            
            if (dish.getDishName() == null || dish.getDishName().isEmpty()) {
                return Result.error("菜品名称不能为空");
            }
            
            // 调用Mapper更新数据
            int rows = dishMapper.updateDish(dish);
            
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("更新菜品失败");
            }
        } catch (Exception e) {
            log.error("更新菜品失败", e);
            return Result.error("更新菜品失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteDish(JSONObject params) {
        try {
            // 获取参数
            Long id = params.getLong("id");
            Long userId = params.getLong("userId");
            
            // 参数验证
            if (id == null) {
                return Result.error("菜品ID不能为空");
            }
            
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            
            // 检查菜品是否存在
            Dish existingDish = dishMapper.getDishById(id);
            if (existingDish == null) {
                return Result.error("菜品不存在或已被删除");
            }
            
            // 调用Mapper删除数据（逻辑删除）
            int rows = dishMapper.deleteDish(id);
            
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("删除菜品失败");
            }
        } catch (Exception e) {
            log.error("删除菜品失败", e);
            return Result.error("删除菜品失败：" + e.getMessage());
        }
    }
}