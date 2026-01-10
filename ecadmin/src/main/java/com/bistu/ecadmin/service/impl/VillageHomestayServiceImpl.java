package com.bistu.ecadmin.service.impl;


import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.TokenUtil;
import com.bistu.ecadmin.dao.DTO.VillageHomestayPageQueryDTO;
import com.bistu.ecadmin.dao.mapper.VillageHomestayMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.VillageHomestay;
import com.bistu.ecadmin.service.VillageHomestayService;
import com.bistu.ecadmin.util.UserContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class VillageHomestayServiceImpl implements VillageHomestayService {

    @Autowired
    private VillageHomestayMapper villageHomestayMapper;
    
    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public PageResult page(VillageHomestayPageQueryDTO dto) {
        // 根据当前登录用户限制可见民宿：
        // - 管理员(userId=10011 或 roleId 包含 1)：查看全部民宿
        // - 普通商家用户：仅查看自己(userId)名下的民宿
        // - 小程序端/匿名访问：没有token时，返回所有已上架的民宿（status=1）

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
                // 商家用户只能查看自己的数据
                dto.setMerchantUserId((long) userInfo.getUserId());
            }
        } else {
            // 小程序端/匿名访问：只显示已上架的数据（status=1）
            if (dto.getStatus() == null) {
                dto.setStatus(1);
            }
        }

        // 设置小程序用户ID（用于 isCollect 计算，不影响权限过滤）
        dto.setUserId(miniProgramUserId);

        int pageNum = dto.getPage() == null || dto.getPage() < 1 ? 1 : dto.getPage();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 10 : dto.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<VillageHomestay> page = villageHomestayMapper.pageQuery(dto);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public VillageHomestay getById(Integer id) {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        return villageHomestayMapper.selectById(id, userId);
    }

    @Override
    public boolean add(VillageHomestay homestay) {
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
                // 商家用户：自动设置userId，状态强制设为待审核（0）
                homestay.setUserId((long) userInfo.getUserId());
                homestay.setStatus(0); // 0-待审核，商家用户新增时强制为待审核状态
            } else {
                // 管理员：如果没有设置userId，设置为null（允许管理员创建时指定）
                // 如果没有设置status，默认为1（营业）
                if (homestay.getStatus() == null) {
                    homestay.setStatus(1); // 1-营业
                }
            }
        } else {
            throw new IllegalArgumentException("未登录，无法新增民宿");
        }

        homestay.setCreateTime(LocalDateTime.now());
        return villageHomestayMapper.insert(homestay) > 0;
    }

    @Override
    public boolean update(VillageHomestay homestay) {
        // 权限校验：商家只能修改自己的数据
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法更新民宿");
        }

        // 查询原数据
        VillageHomestay existing = villageHomestayMapper.selectByIdForCheck(homestay.getId());
        if (existing == null) {
            throw new IllegalArgumentException("民宿不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能修改自己的数据，且不能修改status（不能上架下架）
            if (existing.getUserId() == null || !existing.getUserId().equals((long) userInfo.getUserId())) {
                throw new IllegalArgumentException("无权修改其他商家的民宿");
            }
            // 商家不能修改status，保持原状态
            homestay.setStatus(existing.getStatus());
        }

        return villageHomestayMapper.update(homestay) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        // 权限校验：商家只能删除自己的数据
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法删除民宿");
        }

        // 查询原数据
        VillageHomestay existing = villageHomestayMapper.selectByIdForCheck(id);
        if (existing == null) {
            throw new IllegalArgumentException("民宿不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能删除自己的数据
            if (existing.getUserId() == null || !existing.getUserId().equals((long) userInfo.getUserId())) {
                throw new IllegalArgumentException("无权删除其他商家的民宿");
            }
        }

        return villageHomestayMapper.deleteById(id) > 0;
    }

    @Override
    public List<VillageHomestay> getByVillageId(Integer villageId) {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long miniProgramUserId = UserContext.getUserId();
        
        // 尝试获取管理后台token
        SessionUserInfo userInfo = null;
        if (miniProgramUserId == null) {
            try {
                userInfo = tokenUtil.getUserInfo();
            } catch (Exception e) {
                // 没有管理后台token，这是正常的（小程序端或匿名访问）
                log.debug("未获取到管理后台token（可能是小程序端或匿名访问）: {}", e.getMessage());
            }
        }
        
        // 如果是从管理后台访问，不过滤status；如果是小程序端/匿名访问，只显示已上架的（status=1）
        Integer status = null;
        if (userInfo == null) {
            // 小程序端/匿名访问：只显示已上架的数据（status=1）
            // 注意：即使小程序端用户登录了（有miniProgramUserId），只要没有管理后台token，就应该过滤status
            status = 1;
        }
        
        return villageHomestayMapper.selectByVillageId(villageId, miniProgramUserId, status);
    }

    @Override
    public boolean publish(Integer id) {
        // 只有管理员可以上架
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法上架民宿");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以上架民宿");
        }

        VillageHomestay homestay = new VillageHomestay();
        homestay.setId(id);
        homestay.setStatus(1); // 1-营业（已上架）
        return villageHomestayMapper.update(homestay) > 0;
    }

    @Override
    public boolean unpublish(Integer id) {
        // 只有管理员可以下架
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法下架民宿");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以下架民宿");
        }

        VillageHomestay homestay = new VillageHomestay();
        homestay.setId(id);
        homestay.setStatus(3); // 3-已下架
        return villageHomestayMapper.update(homestay) > 0;
    }
}
