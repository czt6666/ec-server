package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectByUsername(@Param("username") String username);

    int insert(User user);

    User selectById(@Param("id") Long id);

    /**
     * 查询启用状态下的全部用户（仅用于下拉）
     */
    List<User> listActiveUsers();

    /**
     * 查询“民宿用户”下拉选项：
     * - 启用用户（delete_status = '1'）
     * - 且拥有民宿模块的新增权限（villageHomestay:add）
     *
     * 说明：通过 user -> role -> permission 关联过滤，避免硬编码角色ID/名称。
     */
    List<User> listHomestayUserOptions();

    /**
     * 通用：按权限码筛选下拉用户（启用用户且拥有指定 permission_code）。
     * @param permissionCode 例如：villageHomestay:add、restaurant:add、tourCompany:add、studyBase:add、station:add ...
     */
    List<User> listUserOptionsByPermissionCode(@Param("permissionCode") String permissionCode);
}
