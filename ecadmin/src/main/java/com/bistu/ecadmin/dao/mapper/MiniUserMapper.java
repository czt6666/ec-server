package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.pojo.MiniUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 小程序用户 Mapper 接口
 */
@Mapper
public interface MiniUserMapper {

    /**
     * 根据手机号查询小程序用户
     * @param phone 手机号
     * @return 小程序用户
     */
    MiniUser selectByPhone(@Param("phone") String phone);

    /**
     * 根据用户名查询小程序用户
     * @param username 用户名
     * @return 小程序用户
     */
    MiniUser selectByUsername(@Param("username") String username);

    /**
     * 根据ID查询小程序用户
     * @param id 用户ID
     * @return 小程序用户
     */
    MiniUser selectById(@Param("id") Long id);

    /**
     * 新增小程序用户
     * @param miniUser 小程序用户
     * @return 影响行数
     */
    int insert(MiniUser miniUser);

    /**
     * 更新小程序用户信息
     * @param miniUser 小程序用户
     * @return 影响行数
     */
    int update(MiniUser miniUser);

    /**
     * 更新最后登录时间
     * @param id 用户ID
     * @return 影响行数
     */
    int updateLastLoginTime(@Param("id") Long id);
}

