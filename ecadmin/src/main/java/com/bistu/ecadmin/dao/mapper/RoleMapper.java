package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface RoleMapper {
    Role selectByName(@Param("roleName") String roleName);
}
