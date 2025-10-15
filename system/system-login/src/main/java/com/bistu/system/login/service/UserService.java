package com.bistu.system.login.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: zxh
 * @date: 2023/8/31 9:10
 * @description:
 */
public interface UserService {

	public JSONObject listUser(JSONObject jsonObject);

	public JSONObject addUser(JSONObject jsonObject);

	public JSONObject updateUser(JSONObject jsonObject);

	public JSONObject getAllRoles();

	public JSONObject listRole();

	public JSONObject listAllPermission();

	public JSONObject addRole(JSONObject jsonObject);

	public JSONObject updateRole(JSONObject jsonObject);

	public JSONObject deleteRole(JSONObject jsonObject);
}
