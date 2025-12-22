package com.bistu.ecadmin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bistu.ecadmin.dao.DTO.DishCategoryDTO;
import com.bistu.ecadmin.dao.mapper.DishCategoryMapper;
import com.bistu.ecadmin.dao.mapper.UserRoleMapper;
import com.bistu.ecadmin.pojo.DishCategory;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.DishCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 菜品分类Service实现类
 */
@Service
@Slf4j
public class DishCategoryServiceImpl implements DishCategoryService {
    
    @Autowired
    private DishCategoryMapper dishCategoryMapper;
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> addDishCategory(JSONObject params) {
        // 创建菜品分类对象
        DishCategory dishCategory = new DishCategory();
        
        // 设置参数
        String restaurantName = params.getString("restaurantName");
        if (restaurantName != null && !restaurantName.isEmpty()) {
            // 根据店铺名称查询restaurantId
            Long restaurantId = dishCategoryMapper.getRestaurantIdByName(restaurantName);
            if (restaurantId == null) {
                return Result.error("未找到对应的店铺信息");
            }
            dishCategory.setRestaurantId(restaurantId);
        } else {
            // 如果没有提供restaurantName，尝试直接获取restaurantId
            dishCategory.setRestaurantId(params.getLong("restaurantId"));
        }
        
        dishCategory.setCategoryName(params.getString("categoryName"));
        // 设置图片URL
        dishCategory.setImageUrl(params.getString("imageUrl"));
        // 修改这行代码，使用 getIntValue() 方法并手动设置默认值
        Integer sortNum = params.containsKey("sortNum") ? params.getIntValue("sortNum") : 0;
        dishCategory.setSortNum(sortNum);
        
        // 调用Mapper插入数据
        int rows = dishCategoryMapper.addDishCategory(dishCategory);
        
        if (rows > 0) {
            return Result.success();
        } else {
            return Result.error("新增菜品分类失败");
        }
    }
    
    @Override
    public Result<PageResult> listDishCategories(
            Integer page,
            Integer pageSize,
            String categoryName,
            String restaurantName,
            Integer status,
            Long userId
    ) {
        try {
            // 设置默认值
            if (page == null || page < 1) {
                page = 1;
            }
            if (pageSize == null || pageSize < 1) {
                pageSize = 10;
            }
            
            // 计算偏移量
            int offset = (page - 1) * pageSize;

            // 如果提供了餐厅名称，查询对应的餐厅ID
            Long restaurantId = null;
            // 如果通过用户ID查询，则可能拥有多个餐厅
            List<Long> restaurantIds = null;
            if (restaurantName != null && !restaurantName.isEmpty()) {
                restaurantId = dishCategoryMapper.getRestaurantIdByName(restaurantName);
                if (restaurantId == null) {
                    // 如果未找到对应的餐厅，返回空数据
                    return Result.success(new PageResult(0, Collections.emptyList()));
                }
            } else if (userId != null) {
                // 如果没有提供餐厅名称但提供了userId，则根据用户角色判断查询范围
                // 查询用户拥有的角色ID列表
                List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(userId);
                
                // 检查用户是否拥有管理员角色(role_id=1)
                boolean isAdmin = roleIds.contains(1L);

                if (!isAdmin) {
                    // 非管理员用户：查询该用户名下的所有餐厅ID
                    restaurantIds = dishCategoryMapper.listRestaurantIdsByUserId(userId);
                    if (restaurantIds == null || restaurantIds.isEmpty()) {
                        // 该用户暂无绑定餐厅，直接返回空数据
                        return Result.success(new PageResult(0, Collections.emptyList()));
                    }
                }
                // 如果是管理员，则不设置 restaurantId/restaurantIds，查询所有分类
            }
            
            // 查询数据列表
            List<DishCategoryDTO> dishCategories = dishCategoryMapper.listDishCategoriesPaged(
                    categoryName,
                    restaurantId,
                    restaurantIds,
                    status,
                    offset,
                    pageSize
            );
            
            // 查询总数
            int total = dishCategoryMapper.countDishCategories(
                    categoryName,
                    restaurantId,
                    restaurantIds,
                    status
            );
            
            // 构造分页结果
            PageResult pageResult = new PageResult(total, dishCategories);
            
            return Result.success(pageResult);
        } catch (Exception e) {
            log.error("查询菜品分类列表失败", e);
            return Result.error("查询菜品分类列表失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateDishCategory(JSONObject params) {
        try {
            // 验证必填参数
            Long id = params.getLong("id");
            if (id == null) {
                return Result.error("分类ID不能为空");
            }
            
            // 查询原分类信息
            DishCategory existingCategory = dishCategoryMapper.getDishCategoryById(id);
            if (existingCategory == null) {
                return Result.error("菜品分类不存在");
            }
            
            // 创建更新对象
            DishCategory dishCategory = new DishCategory();
            dishCategory.setId(id);
            
            // 设置更新参数
            if (params.containsKey("categoryName")) {
                dishCategory.setCategoryName(params.getString("categoryName"));
            }
            
            if (params.containsKey("restaurantName")) {
                String restaurantName = params.getString("restaurantName");
                if (restaurantName != null && !restaurantName.isEmpty()) {
                    // 根据店铺名称查询restaurantId
                    Long restaurantId = dishCategoryMapper.getRestaurantIdByName(restaurantName);
                    if (restaurantId == null) {
                        return Result.error("未找到对应的店铺信息");
                    }
                    dishCategory.setRestaurantId(restaurantId);
                }
            } else if (params.containsKey("restaurantId")) {
                dishCategory.setRestaurantId(params.getLong("restaurantId"));
            }
            
            if (params.containsKey("sortNum")) {
                dishCategory.setSortNum(params.getIntValue("sortNum"));
            }
            
            // 设置图片URL
            if (params.containsKey("imageUrl")) {
                dishCategory.setImageUrl(params.getString("imageUrl"));
            }
            
            // 移除这行代码，因为DishCategory实体类中没有status字段
            // if (params.containsKey("status")) {
            //     dishCategory.setStatus(params.getInteger("status"));
            // }
            
            // 调用Mapper更新数据
            int rows = dishCategoryMapper.updateDishCategory(dishCategory);
            
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("更新菜品分类失败");
            }
        } catch (Exception e) {
            log.error("更新菜品分类失败", e);
            return Result.error("更新菜品分类失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteDishCategory(JSONObject params) {
        try {
            // 验证必填参数
            Long id = params.getLong("id");
            if (id == null) {
                return Result.error("分类ID不能为空");
            }
            
            // 查询分类是否存在
            DishCategory existingCategory = dishCategoryMapper.getDishCategoryById(id);
            if (existingCategory == null) {
                return Result.error("菜品分类不存在");
            }
            
            // 调用Mapper删除数据（逻辑删除）
            int rows = dishCategoryMapper.deleteDishCategory(id);
            
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("删除菜品分类失败");
            }
        } catch (Exception e) {
            log.error("删除菜品分类失败", e);
            return Result.error("删除菜品分类失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSortNum(Long id, Integer sortNum) {
        DishCategory dishCategory = new DishCategory();
        dishCategory.setId(id);
        dishCategory.setSortNum(sortNum);
        return dishCategoryMapper.updateDishCategory(dishCategory);
    }
    
}