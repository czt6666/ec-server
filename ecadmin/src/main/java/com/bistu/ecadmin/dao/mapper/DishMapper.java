package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.pojo.Dish;
import com.bistu.ecadmin.dao.DTO.DishDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜品Mapper接口
 */
@Mapper
public interface DishMapper {
    
    /**
     * 新增菜品
     * @param dish 菜品信息
     * @return 影响行数
     */
    int addDish(Dish dish);
    
    /**
     * 根据ID查询菜品
     * @param id 菜品ID
     * @return 菜品信息
     */
    Dish getDishById(@Param("id") Long id);
    
    /**
     * 更新菜品
     * @param dish 菜品信息
     * @return 影响行数
     */
    int updateDish(Dish dish);
    
    /**
     * 删除菜品（逻辑删除）
     * @param id 菜品ID
     * @return 影响行数
     */
    int deleteDish(@Param("id") Long id);
    
    /**
     * 分页查询菜品列表
     * @param offset 偏移量
     * @param limit 每页数量
     * @param restaurantName 餐厅名称
     * @param categoryName 分类名称
     * @param dishName 菜品名称
     * @param status 状态
     * @param restaurantIds 餐厅ID列表（通过用户ID查询其名下全部餐厅时使用）
     * @return 菜品列表
     */
    List<DishDTO> listDishes(@Param("offset") int offset,
                             @Param("limit") int limit,
                             @Param("restaurantName") String restaurantName,
                             @Param("categoryName") String categoryName,
                             @Param("dishName") String dishName,
                             @Param("status") Integer status,
                             @Param("restaurantIds") java.util.List<Long> restaurantIds);
    
    /**
     * 根据餐厅ID分页查询菜品列表
     * @param offset 偏移量
     * @param limit 每页数量
     * @param restaurantId 餐厅ID
     * @param categoryName 分类名称
     * @param dishName 菜品名称
     * @param status 状态
     * @return 菜品列表
     */
    List<DishDTO> listDishesByRestaurantId(@Param("offset") int offset, 
                                           @Param("limit") int limit,
                                           @Param("restaurantId") Long restaurantId,
                                           @Param("categoryName") String categoryName,
                                           @Param("dishName") String dishName,
                                           @Param("status") Integer status);
    
    /**
     * 查询菜品总数
     * @param restaurantName 餐厅名称
     * @param categoryName 分类名称
     * @param dishName 菜品名称
     * @param status 状态
     * @param restaurantIds 餐厅ID列表（通过用户ID查询其名下全部餐厅时使用）
     * @return 菜品总数
     */
    int countDishes(@Param("restaurantName") String restaurantName,
                    @Param("categoryName") String categoryName,
                    @Param("dishName") String dishName,
                    @Param("status") Integer status,
                    @Param("restaurantIds") java.util.List<Long> restaurantIds);
    
    /**
     * 根据餐厅ID查询菜品总数
     * @param restaurantId 餐厅ID
     * @param categoryName 分类名称
     * @param dishName 菜品名称
     * @param status 状态
     * @return 菜品总数
     */
    int countDishesByRestaurantId(@Param("restaurantId") Long restaurantId,
                                  @Param("categoryName") String categoryName,
                                  @Param("dishName") String dishName,
                                  @Param("status") Integer status);
    
    /**
     * 更新菜品排序号
     * @param id 菜品ID
     * @param sortNum 排序号
     * @return 影响行数
     */
    int updateSortNum(@Param("id") Long id, @Param("sortNum") Integer sortNum);
}