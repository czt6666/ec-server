package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.dao.DTO.CartAddDTO;
import com.bistu.ecadmin.dao.DTO.CartPageDTO;
import com.bistu.ecadmin.pojo.Cart;
import com.bistu.ecadmin.pojo.CartVO;
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

	@PostMapping("/add")
	public Result add(@RequestBody CartAddDTO req) {
		try {
			cartService.addToCart(req.getUserId(), req.getSkuId());
			return Result.success();
		} catch (RuntimeException e) {
			// 捕获商品下架等异常，返回错误信息
			return Result.error(e.getMessage());
		} catch (Exception e) {
			// 捕获其他异常
			return Result.error("加入购物车失败：" + e.getMessage());
		}
	}

	@GetMapping("/list")
	public Result list(@RequestParam int userId) {
		List<CartVO> data = cartService.listByUserWithProduct(userId);
		return Result.success(data);
	}

	@GetMapping("/page")
	public Result page(CartPageDTO req) {
		if (req.getPage() == null || req.getPageSize() == null) {
			return Result.error("分页参数不能为空");
		}
		int page = req.getPage();
		int pageSize = req.getPageSize();
		int offset = (page - 1) * pageSize;

		List<CartVO> list;
		int total;
		if (req.getUserId() != null) {
			list = cartService.listByUserPagedWithProduct(req.getUserId(), offset, pageSize);
			total = cartService.countByUser(req.getUserId());
		} else {
			list = cartService.listAllPagedWithProduct(offset, pageSize);
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
