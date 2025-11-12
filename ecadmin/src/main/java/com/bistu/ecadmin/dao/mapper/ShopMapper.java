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
                         @Param("offset") Integer offset,
                         @Param("limit") Integer limit);
    int countShops(@Param("shopName") String shopName,
                   @Param("productType") String productType,
                   @Param("businessStatus") Integer businessStatus,
                   @Param("village") String village);
    int countByShopName(@Param("shopName") String shopName,
                        @Param("excludeId") Long excludeId);
    int getMaxDisplayNo();
    Integer getDisplayNoById(Long id);
    int resequenceDisplayNoAfterDelete(@Param("displayNo") int displayNo);
}
