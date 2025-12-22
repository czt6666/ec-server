package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.dao.DTO.DishCategoryDTO;
import com.bistu.ecadmin.pojo.DishCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜品分类Mapper接口
 */
@Mapper
public interface DishCategoryMapper {
    
    /**
     * 新增菜品分类
     * @param dishCategory 菜品分类信息
     * @return 影响行数
     */
    int addDishCategory(DishCategory dishCategory);
    
    /**
     * 根据店铺名称查询restaurantId
     * @param restaurantName 店铺名称
     * @return restaurantId
     */
    Long getRestaurantIdByName(@Param("restaurantName") String restaurantName);
    
    /**
     * 根据用户ID查询该用户名下所有餐厅ID
     * @param userId 用户ID
     * @return 餐厅ID列表
     */
    List<Long> listRestaurantIdsByUserId(@Param("userId") Long userId);
    
    /**
     * 分页查询菜品分类列表
     * @param categoryName 菜品分类名称
     * @param restaurantId 餐厅ID（通过餐厅名称精确查询时使用）
     * @param restaurantIds 餐厅ID列表（通过用户ID查询其名下全部餐厅时使用）
     * @param status 状态
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @return 菜品分类列表
     */
    List<DishCategoryDTO> listDishCategoriesPaged(
            @Param("categoryName") String categoryName,
            @Param("restaurantId") Long restaurantId,
            @Param("restaurantIds") List<Long> restaurantIds,
            @Param("status") Integer status,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );
    
    /**
     * 查询菜品分类总数
     * @param categoryName 菜品分类名称
     * @param restaurantId 餐厅ID（通过餐厅名称精确查询时使用）
     * @param restaurantIds 餐厅ID列表（通过用户ID查询其名下全部餐厅时使用）
     * @param status 状态
     * @return 总数
     */
    int countDishCategories(
            @Param("categoryName") String categoryName,
            @Param("restaurantId") Long restaurantId,
            @Param("restaurantIds") List<Long> restaurantIds,
            @Param("status") Integer status
    );
    
    /**
     * 更新菜品分类
     * @param dishCategory 菜品分类信息
     * @return 影响行数
     */
    int updateDishCategory(DishCategory dishCategory);
    
    /**
     * 根据ID删除菜品分类（逻辑删除）
     * @param id 分类ID
     * @return 影响行数
     */
    int deleteDishCategory(@Param("id") Long id);
    
    /**
     * 根据ID查询菜品分类
     * @param id 分类ID
     * @return 菜品分类信息
     */
    DishCategory getDishCategoryById(@Param("id") Long id);
    
    /**
     * 更新菜品分类排序号（复用updateDishCategory方法）
     * @param id 菜品分类ID
     * @param sortNum 排序号
     * @return 影响行数
     */
    int updateSortNum(@Param("id") Long id, @Param("sortNum") Integer sortNum);
}