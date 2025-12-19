package com.bistu.ecadmin.service;

import com.bistu.ecadmin.dao.mapper.UserMapper;
import com.bistu.ecadmin.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserHeaderService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户ID从数据库查询用户名
     * @param userId 用户ID
     * @return 用户名
     */
    public String getUsernameByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        
        User user = userMapper.selectById(userId);
        return user != null ? user.getUsername() : null;
    }
}