package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.pojo.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ShopMapper {
    int addShop(Shop shop);
    int updateShop(Shop shop);
    int deleteShopById(Long id);
    Shop getShopById(Long id);
    Shop getShopByUserId(Long userId);
    List<Shop> listShops(@Param("shopName") String shopName,
                         @Param("productType") String productType,
                         @Param("businessStatus") Integer businessStatus,
                         @Param("village") String village,
                         @Param("userId") Long userId,
                         @Param("offset") Integer offset,
                         @Param("limit") Integer limit);
    int countShops(@Param("shopName") String shopName,
                   @Param("productType") String productType,
                   @Param("businessStatus") Integer businessStatus,
                   @Param("village") String village,
                   @Param("userId") Long userId);
    int countByShopName(@Param("shopName") String shopName,
                        @Param("excludeId") Long excludeId);
    int getMaxDisplayNo();
    Integer getDisplayNoById(Long id);
    int resequenceDisplayNoAfterDelete(@Param("displayNo") int displayNo);
    
    /**
     * 查询店铺选项列表（用于下拉选择）
     * @param userId 用户ID
     * @param isAdmin 是否是管理员（true: 查询全部店铺, false: 只查询该用户关联的店铺）
     * @return 店铺列表
     */
    List<Shop> listShopOptions(@Param("userId") Long userId, @Param("isAdmin") Boolean isAdmin);
}
