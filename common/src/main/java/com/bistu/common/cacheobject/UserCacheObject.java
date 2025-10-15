package com.bistu.common.cacheobject;

import lombok.Data;

import java.util.List;
import java.util.Set;


/**
 * 用户缓存对象
 */
@Data
public class UserCacheObject {
    private int userId;
    private String username;
    private String nickname;
    private List<Integer> roleIds;
    private Set<String> menuList;
    private Set<String> permissionList;
}
