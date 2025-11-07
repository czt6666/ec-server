package com.bistu.ecadmin.controller;


import com.bistu.ecadmin.dao.DTO.CartAddDTO;
import com.bistu.ecadmin.dao.DTO.CartPageDTO;
import com.bistu.ecadmin.pojo.Cart;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.impl.CartServiceIml;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/ecadmin/cart")
public class CartController {
    @Autowired
	private CartServiceIml cartService;
	// CartController.java

	@PostMapping("/add")
	public Result add(@RequestBody CartAddDTO req) {
		cartService.addToCart(req.getUserId(), req.getSkuId());
		return Result.success();
	}
	@GetMapping("/list")
	public Result list(@RequestParam int userId) {
		List<Cart> data = cartService.listByUser(userId);
		return Result.success(data);
	}
	// com.bistu.ecadmin.controller.CartController
	@GetMapping("/page")
	public Result page(CartPageDTO req) {
		if (req.getPage() == null || req.getPageSize() == null) {
			return Result.error("分页参数不能为空");
		}
		int page = req.getPage();
		int pageSize = req.getPageSize();
		int offset = (page - 1) * pageSize;

		List<Cart> list;
		int total;
		if (req.getUserId() != null) {
			list = cartService.listByUserPaged(req.getUserId(), offset, pageSize);
			total = cartService.countByUser(req.getUserId());
		} else {
			list = cartService.listAllPaged(offset, pageSize);
			total = cartService.countAll();
		}

		Map<String, Object> data = new HashMap<>();
		data.put("list", list);
		data.put("total", total);
		data.put("page", page);
		data.put("pageSize", pageSize);
		return Result.success(data);
	}
	@DeleteMapping("/{skuId}")
	public Result delete(@RequestParam int userId, @PathVariable int skuId) {
		boolean removed = cartService.deleteItem(userId, skuId);
		if (!removed) {
			return Result.error("购物车中未找到该商品");
		}
		return Result.success();
	}



}
