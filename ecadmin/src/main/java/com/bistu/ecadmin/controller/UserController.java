package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.dao.mapper.UserMapper;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户相关接口
 */
@RestController
@Controller("ecAdminUserController")
@RequestMapping("/admin/ecadmin/user")
@Api(tags = "用户管理")
@Slf4j
public class UserController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取启用状态下的全部用户（用于前端下拉）
     */
    @GetMapping("/options")
    @ApiOperation("获取用户下拉选项")
    public Result<List<User>> listUserOptions(@RequestParam(required = false) String permissionCode) {
        try {
            // 不传 permissionCode：兼容旧逻辑，返回所有启用用户
            // 传入 permissionCode：按“拥有该权限”的用户过滤（适用于民宿/旅游/研学/养老/餐饮等模块的商户绑定）
            List<User> users;
            if (permissionCode == null || permissionCode.trim().isEmpty()) {
                users = userMapper.listActiveUsers();
            } else if ("villageHomestay:add".equals(permissionCode.trim())) {
                // 兼容已有专用SQL（也可直接走通用方法）
                users = userMapper.listHomestayUserOptions();
            } else {
                users = userMapper.listUserOptionsByPermissionCode(permissionCode.trim());
            }
            return Result.success(users);
        } catch (Exception e) {
            log.error("获取用户下拉列表失败", e);
            return Result.error("获取用户列表失败：" + e.getMessage());
        }
    }
}

