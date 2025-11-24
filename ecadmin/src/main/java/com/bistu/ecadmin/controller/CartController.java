package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.dao.DTO.CartAddDTO;
import com.bistu.ecadmin.dao.DTO.CartPageDTO;
import com.bistu.ecadmin.pojo.Cart;
import com.bistu.ecadmin.pojo.CartVO;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.impl.CartServiceIml;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/ecadmin/cart")
@Api(tags = "购物车管理")
public class CartController {
	@Autowired
	private CartServiceIml cartService;

	@PostMapping("/add")
	@ApiOperation("添加商品到购物车")
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
	@ApiOperation("获取用户购物车列表")
	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int", paramType = "query")
	public Result list(@RequestParam int userId) {
		List<CartVO> data = cartService.listByUserWithProduct(userId);
		return Result.success(data);
	}

	@GetMapping("/page")
	@ApiOperation("分页获取购物车列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Integer", paramType = "query")
	})
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
	@ApiOperation("从购物车删除商品")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "skuId", value = "商品SKU ID", required = true, dataType = "int", paramType = "path")
	})
	public Result delete(@RequestParam int userId, @PathVariable int skuId) {
		boolean removed = cartService.deleteItem(userId, skuId);
		if (!removed) {
			return Result.error("购物车中未找到该商品");
		}
		return Result.success();
	}
}