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
}
