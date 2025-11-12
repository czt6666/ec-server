package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.pojo.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    int insert(UserRole userRole);
}
