package com.bistu.ecadmin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.TokenUtil;
import com.bistu.ecadmin.dao.mapper.*;
import com.bistu.ecadmin.pojo.*;
import com.bistu.ecadmin.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 店铺服务实现类
 */
@Service
@Slf4j
public class ShopServiceImpl implements ShopService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopProductMapper shopProductMapper;

    @Autowired
    private TokenUtil tokenUtil;




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
            if (shop.getShopName() == null || shop.getShopName().trim().isEmpty()) {
                return Result.error("店铺名称不能为空");
            }
            if (shop.getShopAbbreviation() == null || shop.getShopAbbreviation().trim().isEmpty()) {
                return Result.error("店铺缩写不能为空");
            }

            int count = shopMapper.countByShopName(shop.getShopName(), null);
            if (count > 0) {
                return Result.error("店铺名称已存在");
            }

            // 1. 创建默认账号（用户名=店铺缩写，密码=123456）
            String username = shop.getShopAbbreviation().trim();
            Long userId = createUserAccount(username, DEFAULT_PASSWORD, shop.getShopName());
            if (userId == null) {
                return Result.error("创建店铺失败：账号创建失败");
            }

            // 2. 分配“农产品商户”角色
            assignRole(userId, MERCHANT_ROLE_NAME);

            // 3. 回写账号ID
            shop.setUserId(userId);

            // 4. 生成展示顺序并保存店铺
            int maxDisplayNo = shopMapper.getMaxDisplayNo();
            shop.setDisplayNo(maxDisplayNo + 1);

            int result = shopMapper.addShop(shop);
            if (result > 0) {
                Shop createdShop = shopMapper.getShopById(shop.getId());
                return Result.success(createdShop);
            }
            return Result.error("创建店铺失败");
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

            // 按当前登录用户限制可见店铺：
            // - 管理员(userId=10011 或 roleId 包含 1)：查看全部店铺
            // - 普通用户：仅查看自己(userId)名下的店铺
            Long userIdFilter = null;
            try {
                SessionUserInfo userInfo = tokenUtil.getUserInfo();
                if (userInfo != null) {
                    List<Integer> roleIds = userInfo.getRoleIds();
                    boolean isAdmin = (userInfo.getUserId() == 10011)
                            || (roleIds != null && roleIds.contains(1));
                    if (!isAdmin) {
                        userIdFilter = (long) userInfo.getUserId();
                    }
                }
            } catch (Exception e) {
                log.warn("获取当前登录用户信息失败，店铺列表默认不过滤用户: {}", e.getMessage());
            }

            // 查询列表
            List<Shop> shops = shopMapper.listShops(shopName, productType, businessStatus, village, userIdFilter, offset, pageSize);

            // 查询总数
            int total = shopMapper.countShops(shopName, productType, businessStatus, village, userIdFilter);

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
            User existing = userMapper.selectByUsername(username);
            if (existing != null) {
                log.warn("用户名已存在: {}", username);
                return null;
            }

            String encodedPassword = passwordEncoder != null
                    ? passwordEncoder.encode(password)
                    : DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

            User user = new User();
            user.setUsername(username);
            user.setPassword(encodedPassword);
            user.setNickname(nickname);
            user.setDeleteStatus(1);
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());

            int inserted = userMapper.insert(user);
            if (inserted > 0 && user.getId() != null) {
                return user.getId();
            }
            log.warn("创建用户失败: {}", username);
            return null;
        } catch (Exception e) {
            log.error("创建用户账号失败", e);
            throw e;
        }
    }

    /**
     * 分配角色
     */
    private boolean assignRole(Long userId, String roleName) {
        try {
            Role role = roleMapper.selectByName(roleName);
            if (role == null) {
                log.warn("角色不存在: {}", roleName);
                return false;
            }

            UserRole bind = new UserRole();
            bind.setUserId(userId);
            bind.setRoleId(role.getId());

            return userRoleMapper.insert(bind) > 0;
        } catch (Exception e) {
            log.error("分配角色失败", e);
            throw e;
        }
    }

    @Override
    public Result<List<JSONObject>> listProductsByShop(Long shopId, Integer status) {
        try {
            if (shopId == null || shopId <= 0) {
                return Result.error("店铺ID不能为空");
            }
            Shop shop = shopMapper.getShopById(shopId);
            if (shop == null) {
                return Result.error("店铺不存在");
            }

            List<JSONObject> products = shopProductMapper.listProductsByShopId(shopId, status);
            return Result.success(products == null ? Collections.emptyList() : products);
        } catch (Exception e) {
            log.error("查询商家商品失败", e);
            return Result.error("查询商家商品失败：" + e.getMessage());
        }
    }
}
