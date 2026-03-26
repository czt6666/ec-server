package com.bistu.ecadmin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.TokenUtil;
import com.bistu.ecadmin.dao.DTO.RestaurantQueryDTO;
import com.bistu.ecadmin.dao.mapper.RestaurantMapper;
import com.bistu.ecadmin.dao.mapper.UserMapper;
import com.bistu.ecadmin.dao.mapper.VillageMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Restaurant;
import com.bistu.ecadmin.service.RestaurantService;
import com.bistu.ecadmin.util.UserContext;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantMapper restaurantMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VillageMapper villageMapper;
    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public PageResult list(RestaurantQueryDTO dto) {
        // 根据当前登录用户限制可见门店：
        // - 管理员(userId=10011 或 roleId 包含 1)：查看全部门店
        // - 普通用户：仅查看自己(userId)名下的门店
        // - 小程序端/匿名访问：没有token时，返回所有门店（不过滤）

        // 优先从UserContext获取userId（小程序端JWT token）
        Long miniProgramUserId = UserContext.getUserId();

        // 如果UserContext中没有，尝试从TokenUtil获取（管理后台token）
        SessionUserInfo userInfo = null;
        if (miniProgramUserId == null) {
            try {
                userInfo = tokenUtil.getUserInfo();
            } catch (Exception e) {
                // 没有管理后台token，这是正常的（小程序端或匿名访问）
                log.debug("未获取到管理后台token（可能是小程序端或匿名访问）: {}", e.getMessage());
            }
        }

        // 如果从token获取到用户信息，进行权限过滤
        if (userInfo != null) {
            List<Integer> roleIds = userInfo.getRoleIds();
            boolean isAdmin = (userInfo.getUserId() == 10011)
                    || (roleIds != null && roleIds.contains(1));
            if (!isAdmin) {
                dto.setUserId((long) userInfo.getUserId());
            }
        } else {
            // 小程序端/匿名访问：只显示营业中的门店（status=1）
            if (dto.getStatus() == null) {
                dto.setStatus(1);
            }
        }

        // 设置小程序用户ID（用于 isCollect 计算，不影响权限过滤）
        dto.setMiniProgramUserId(miniProgramUserId);

        dto.setPageNum(dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum());
        dto.setPageSize(dto.getPageSize() == null || dto.getPageSize() < 1 ? 10 : dto.getPageSize());
        dto.setOffset((dto.getPageNum() - 1) * dto.getPageSize());

        List<Restaurant> records = restaurantMapper.list(dto);
        int total = restaurantMapper.count(dto);
        return new PageResult(total, records);
    }

    @Override
    public Restaurant getById(Long id) {
        // 从 UserContext 获取小程序用户ID（用于 isCollect 计算）
        Long miniProgramUserId = UserContext.getUserId();
        return restaurantMapper.selectById(id, miniProgramUserId);
    }

    @Override
    public void create(Restaurant restaurant) {
        // 获取当前登录用户信息
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            log.debug("未获取到管理后台token: {}", e.getMessage());
        }

        if (userInfo != null) {
            // 判断是否为管理员
            List<Integer> roleIds = userInfo.getRoleIds();
            boolean isAdmin = (userInfo.getUserId() == 10011)
                    || (roleIds != null && roleIds.contains(1));

            if (!isAdmin) {
                // 普通商户：自动设置userId，状态强制设为待审核（2）
                restaurant.setUserId((long) userInfo.getUserId());
                restaurant.setStatus(2); // 2-待审核，普通商户新增时强制为待审核状态
            } else {
                // 管理员：如果没有设置status，默认为1（营业）
                if (restaurant.getStatus() == null) {
                    restaurant.setStatus(1); // 1-营业
                }
            }
        } else {
            throw new IllegalArgumentException("未登录，无法新增门店");
        }

        // 如果没有设置sortOrder，自动设置为该用户下最大的sortOrder + 1
        if (restaurant.getSortOrder() == null) {
            Integer maxSortOrder = restaurantMapper.getMaxSortOrder(restaurant.getUserId());
            restaurant.setSortOrder(maxSortOrder != null ? maxSortOrder + 1 : 1);
        }

        validate(restaurant, null);
        restaurantMapper.insert(restaurant);
    }

    @Override
    public void update(Restaurant restaurant) {
        // 权限校验：普通商户只能修改自己的门店，且不能修改 status
        if (restaurant.getId() != null) {
            Restaurant existing = restaurantMapper.selectById(restaurant.getId(), null);
            if (existing != null) {
                try {
                    SessionUserInfo userInfo = tokenUtil.getUserInfo();
                    if (userInfo != null) {
                        List<Integer> roleIds = userInfo.getRoleIds();
                        boolean isAdmin = (userInfo.getUserId() == 10011)
                                || (roleIds != null && roleIds.contains(1));
                        if (!isAdmin) {
                            // 普通商户：只能修改自己的门店，且不能修改 status
                            if (!existing.getUserId().equals((long) userInfo.getUserId())) {
                                throw new IllegalArgumentException("无权修改其他商户的门店");
                            }
                            // 保持原有的 status，不允许修改
                            restaurant.setStatus(existing.getStatus());
                        }
                        // 管理员可以修改任何门店和 status
                    }
                } catch (Exception e) {
                    log.debug("未获取到管理后台token: {}", e.getMessage());
                    // 如果没有 token，按普通商户处理
                    restaurant.setStatus(existing.getStatus());
                }
            }
        }

        validate(restaurant, restaurant.getId());
        restaurantMapper.update(restaurant);
    }

    @Override
    public void delete(Long id) {
        restaurantMapper.deleteById(id);
    }

    @Override
    public List<String> listNamesByUser(Long userId) {
        return restaurantMapper.listNamesByUserId(userId);
    }

    @Override
    public Long getIdByName(String name) {
        return restaurantMapper.getIdByName(name);
    }

    @Override
    public List<Restaurant> listByUser(Long userId) {
        return restaurantMapper.listByUserId(userId);
    }

    @Override
    public void swapSortOrder(Long id1, Long id2) {
        Restaurant r1 = restaurantMapper.selectById(id1, null);
        Restaurant r2 = restaurantMapper.selectById(id2, null);
        
        if (r1 == null || r2 == null) {
            throw new IllegalArgumentException("餐厅不存在");
        }
        
        Integer sortOrder1 = r1.getSortOrder();
        Integer sortOrder2 = r2.getSortOrder();
        
        // 如果两个sortOrder相同，不需要交换
        if (sortOrder1 != null && sortOrder1.equals(sortOrder2)) {
            return;
        }
        
        // 交换sortOrder
        r1.setSortOrder(sortOrder2);
        r2.setSortOrder(sortOrder1);
        
        restaurantMapper.update(r1);
        restaurantMapper.update(r2);
    }

    private void validate(Restaurant r, Long excludeId) {
        if (r == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(r.getName()) || r.getName().length() > 100) {
            throw new IllegalArgumentException("门店名称不能为空且不超过100字符");
        }
        if (r.getUserId() == null || userMapper.selectById(r.getUserId()) == null) {
            throw new IllegalArgumentException("关联用户不存在");
        }
        if (r.getVillageId() == null || villageMapper.getById(r.getVillageId()) == null) {
            throw new IllegalArgumentException("所属村不存在");
        }
        if (StringUtils.isBlank(r.getBusinessStartTime()) || !isHHmm(r.getBusinessStartTime())) {
            throw new IllegalArgumentException("开始营业时间格式不正确");
        }
        if (StringUtils.isBlank(r.getBusinessEndTime()) || !isHHmm(r.getBusinessEndTime())) {
            throw new IllegalArgumentException("结束营业时间格式不正确");
        }
        if (StringUtils.isBlank(r.getAddress()) || r.getAddress().length() > 200) {
            throw new IllegalArgumentException("门店地址不能为空且不超过200字符");
        }
        if (StringUtils.length(r.getPhone()) > 20) {
            throw new IllegalArgumentException("联系电话不超过20字符");
        }
        if (StringUtils.length(r.getNotice()) > 500) {
            throw new IllegalArgumentException("门店公告不超过500字符");
        }
        checkLicenseLimit(r.getLicenseUrls());
    }

    private boolean isHHmm(String time) {
        try {
            LocalTime.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void checkLicenseLimit(String licenseJson) {
        if (StringUtils.isBlank(licenseJson)) {
            return;
        }
        JSONArray arr = JSONArray.parseArray(licenseJson);
        long businessCount = arr.stream()
                .map(o -> (JSONObject) o)
                .filter(obj -> "business".equals(obj.getString("type")))
                .count();
        long foodCount = arr.stream()
                .map(o -> (JSONObject) o)
                .filter(obj -> "food".equals(obj.getString("type")))
                .count();
        if (businessCount > 15) {
            throw new IllegalArgumentException("营业执照最多上传15张");
        }
        if (foodCount > 15) {
            throw new IllegalArgumentException("食品经营许可证最多上传15张");
        }
    }
}
