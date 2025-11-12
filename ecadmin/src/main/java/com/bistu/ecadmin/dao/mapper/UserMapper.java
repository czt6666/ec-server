package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User selectByUsername(@Param("username") String username);
    int insert(User user);
}
