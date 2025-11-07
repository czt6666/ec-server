package com.bistu.ecadmin.service;



import com.bistu.ecadmin.pojo.Cart;

import java.util.List;

public interface CartService {
	void addToCart(int userId, int skuId);
	List<Cart> listByUser(int userId);
	boolean deleteItem(int userId, int skuId);

	// 新增分页
	List<Cart> listByUserPaged(int userId, int offset, int pageSize);
	int countByUser(int userId);
	// CartService.java
	List<Cart> listAllPaged(int offset, int pageSize);
	int countAll();
}
