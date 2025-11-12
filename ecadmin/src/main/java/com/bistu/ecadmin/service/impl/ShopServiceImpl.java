package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.mapper.ShopMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.Shop;
import com.bistu.ecadmin.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 店铺服务实现类
 */
@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    // TODO: 需要注入UserMapper、RoleMapper、UserRoleMapper
    // @Autowired
    // private UserMapper userMapper;
    // @Autowired
    // private RoleMapper roleMapper;
    // @Autowired
    // private UserRoleMapper userRoleMapper;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder; // 密码加密器，如果没有可以手动加密

    // 默认密码
    private static final String DEFAULT_PASSWORD = "123456";
    // 农产品商户角色名称
    private static final String MERCHANT_ROLE_NAME = "农产品商户";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Shop> createShop(Shop shop) {
        try {
            int count = shopMapper.countByShopName(shop.getShopName(), null);
            if (count > 0) {
                return Result.error("店铺名称已存在");
            }
            if (shop.getShopAbbreviation() == null || shop.getShopAbbreviation().trim().isEmpty()) {
                return Result.error("店铺缩写不能为空");
            }

            // 临时放开账号创建校验，后续接入用户体系后再恢复
            shop.setUserId(null);

            int maxDisplayNo = shopMapper.getMaxDisplayNo();
            shop.setDisplayNo(maxDisplayNo + 1);

            int result = shopMapper.addShop(shop);
            if (result > 0) {
                Shop createdShop = shopMapper.getShopById(shop.getId());
                return Result.success(createdShop);
            } else {
                return Result.error("创建店铺失败");
            }
        } catch (Exception e) {
            log.error("创建店铺失败", e);
            throw new RuntimeException("创建店铺失败：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Shop> updateShop(Shop shop) {
        try {
            // 校验店铺是否存在
            if (shop.getId() == null) {
                return Result.error("店铺ID不能为空");
            }

            Shop existingShop = shopMapper.getShopById(shop.getId());
            if (existingShop == null) {
                return Result.error("店铺不存在");
            }

            // 校验店铺名称是否重复
            if (shop.getShopName() != null) {
                int count = shopMapper.countByShopName(shop.getShopName(), shop.getId());
                if (count > 0) {
                    return Result.error("店铺名称已存在");
                }
            }

            // 更新店铺信息（displayNo 仅当传入非空时才更新）
            int result = shopMapper.updateShop(shop);
            if (result > 0) {
                Shop updatedShop = shopMapper.getShopById(shop.getId());
                return Result.success(updatedShop);
            } else {
                return Result.error("更新店铺失败");
            }
        } catch (Exception e) {
            log.error("更新店铺失败", e);
            throw new RuntimeException("更新店铺失败：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteShop(Long shopId) {
        try {
            if (shopId == null || shopId <= 0) {
                return Result.error("店铺ID不能为空");
            }

            Shop shop = shopMapper.getShopById(shopId);
            if (shop == null) {
                return Result.error("店铺不存在");
            }

            // 记录被删除店铺的展示顺序号
            Integer displayNo = shopMapper.getDisplayNoById(shopId);
            if (displayNo == null) {
                return Result.error("店铺展示顺序不存在");
            }

            // 删除店铺
            int result = shopMapper.deleteShopById(shopId);
            if (result > 0) {
                // 将后续店铺的展示顺序号全部减 1，保持连续
                shopMapper.resequenceDisplayNoAfterDelete(displayNo);
                return Result.success("删除成功");
            } else {
                return Result.error("删除店铺失败");
            }
        } catch (Exception e) {
            log.error("删除店铺失败", e);
            throw new RuntimeException("删除店铺失败：" + e.getMessage(), e);
        }
    }

    @Override
    public Result<PageResult> listShops(String shopName, String productType,
                                        Integer businessStatus, String village,
                                        Integer pageNum, Integer pageSize) {
        try {
            // 设置默认值
            if (pageNum == null || pageNum < 1) {
                pageNum = 1;
            }
            if (pageSize == null || pageSize < 1) {
                pageSize = 10;
            }

            // 计算偏移量
            int offset = (pageNum - 1) * pageSize;

            // 查询列表
            List<Shop> shops = shopMapper.listShops(shopName, productType, businessStatus, village, offset, pageSize);

            // 查询总数
            int total = shopMapper.countShops(shopName, productType, businessStatus, village);

            PageResult pageResult = new PageResult(total, shops);
            return Result.success(pageResult);
        } catch (Exception e) {
            log.error("查询店铺列表失败", e);
            return Result.error("查询店铺列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Shop> getShopById(Long shopId) {
        try {
            if (shopId == null || shopId <= 0) {
                return Result.error("店铺ID不能为空");
            }

            Shop shop = shopMapper.getShopById(shopId);
            if (shop == null) {
                return Result.error("店铺不存在");
            }

            return Result.success(shop);
        } catch (Exception e) {
            log.error("查询店铺详情失败", e);
            return Result.error("查询店铺详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建用户账号
     */
    private Long createUserAccount(String username, String password, String nickname) {
        try {
            // TODO: 需要实现UserMapper相关方法
            // 1. 检查用户名是否已存在
            // 2. 加密密码
            // 3. 创建用户
            // 4. 返回用户ID
            log.warn("UserMapper未实现，无法创建用户账号");
            return null;
        } catch (Exception e) {
            log.error("创建用户账号失败", e);
            return null;
        }
    }

    /**
     * 分配角色
     */
    private boolean assignRole(Long userId, String roleName) {
        try {
            // TODO: 需要实现RoleMapper和UserRoleMapper相关方法
            // 1. 根据角色名称查询角色ID
            // 2. 关联用户和角色
            log.warn("RoleMapper和UserRoleMapper未实现，无法分配角色");
            return false;
        } catch (Exception e) {
            log.error("分配角色失败", e);
            return false;
        }
    }
}
