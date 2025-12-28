package com.bistu.ecadmin.controller;

import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.TokenUtil;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.UserCollect;
import com.bistu.ecadmin.service.UserCollectService;
import com.bistu.ecadmin.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ecadmin/collect")
@Api(tags = "用户收藏管理")
@Slf4j
public class UserCollectController {

    @Autowired
    private UserCollectService userCollectService;
    
    @Autowired
    private TokenUtil tokenUtil;

    @GetMapping("/list")
    @ApiOperation("分页查询收藏列表（返回收藏总数和是否收藏标记，无token时使用匿名用户ID=0）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID（查询该用户的收藏列表，不传则从token获取，无token时使用0）", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "targetType", value = "收藏对象类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentUserId", value = "当前用户ID（用于判断是否收藏，可选，不传则使用userId）", dataType = "Long", paramType = "query")
    })
    public Result<PageResult<UserCollect>> list(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer limit,
                                                @RequestParam(required = false) Long userId,
                                                @RequestParam(required = false) String targetType,
                                                @RequestParam(required = false) Long currentUserId) {
        // 如果传入了userId，直接使用
        Long finalUserId = userId;
        
        // 如果没有传入userId，尝试从token获取
        if (finalUserId == null) {
            // 优先从UserContext获取userId（小程序端JWT token）
            finalUserId = UserContext.getUserId();
            
            // 如果UserContext中没有，尝试从TokenUtil获取（管理后台token）
            if (finalUserId == null) {
                try {
                    SessionUserInfo userInfo = tokenUtil.getUserInfo();
                    if (userInfo != null && userInfo.getUserId() > 0) {
                        finalUserId = (long) userInfo.getUserId();
                    }
                } catch (Exception e) {
                    log.debug("从TokenUtil获取用户信息失败（可能是小程序端或匿名访问）: {}", e.getMessage());
                }
            }
            
            // 如果仍然为null，说明是匿名用户，使用匿名用户ID=0
            if (finalUserId == null) {
                finalUserId = 0L;
                log.debug("匿名用户查询收藏列表，使用userId=0");
            }
        }
        
        // 如果currentUserId未传入，使用finalUserId
        Long finalCurrentUserId = (currentUserId != null) ? currentUserId : finalUserId;
        
        return userCollectService.list(page, limit, finalUserId, targetType, finalCurrentUserId);
    }

    @PostMapping("/create")
    @ApiOperation("新增收藏（从token中获取userId，无token时使用匿名用户ID=0）")
    public Result<?> create(@RequestBody UserCollect collect) {
        // 优先从UserContext获取userId（小程序端JWT token）
        Long userId = UserContext.getUserId();
        
        // 如果UserContext中没有，尝试从TokenUtil获取（管理后台token）
        if (userId == null) {
            try {
                SessionUserInfo userInfo = tokenUtil.getUserInfo();
                if (userInfo != null && userInfo.getUserId() > 0) {
                    userId = (long) userInfo.getUserId();
                }
            } catch (Exception e) {
                log.debug("从TokenUtil获取用户信息失败（可能是小程序端或匿名访问）: {}", e.getMessage());
            }
        }
        
        // 如果从token获取到userId，则使用它；否则使用传入的userId；如果都没有，使用匿名用户ID=0
        if (userId != null) {
            collect.setUserId(userId);
        } else if (collect.getUserId() == null) {
            // 没有token且没有传入userId，使用匿名用户ID=0
            collect.setUserId(0L);
            log.debug("匿名用户点赞/收藏，使用userId=0");
        }
        
        return userCollectService.create(collect);
    }

    @GetMapping("/get")
    @ApiOperation("查询是否收藏（从token中获取userId，无token时使用匿名用户ID=0）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID（可选，不传则从token获取）", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "targetType", value = "收藏对象类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "targetId", value = "收藏对象ID", required = true, dataType = "String", paramType = "query")
    })
    public Result<UserCollect> get(@RequestParam(required = false) Long userId,
                                   @RequestParam String targetType,
                                   @RequestParam String targetId) {
        // 优先从UserContext获取userId（小程序端JWT token）
        Long finalUserId = UserContext.getUserId();
        
        // 如果UserContext中没有，尝试从TokenUtil获取（管理后台token）
        if (finalUserId == null) {
            try {
                SessionUserInfo userInfo = tokenUtil.getUserInfo();
                if (userInfo != null && userInfo.getUserId() > 0) {
                    finalUserId = (long) userInfo.getUserId();
                }
            } catch (Exception e) {
                log.debug("从TokenUtil获取用户信息失败（可能是小程序端或匿名访问）: {}", e.getMessage());
            }
        }
        
        // 如果从token获取失败，使用传入的userId参数
        if (finalUserId == null) {
            finalUserId = userId;
        }
        
        // 如果仍然为null，说明是匿名用户，使用匿名用户ID=0
        if (finalUserId == null) {
            finalUserId = 0L;
            log.debug("匿名用户查询收藏状态，使用userId=0");
        }
        
        return userCollectService.get(finalUserId, targetType, targetId);
    }

    @DeleteMapping("/delete")
    @ApiOperation("取消收藏（从token中获取userId，无token时使用匿名用户ID=0）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID（可选，不传则从token获取）", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "targetType", value = "收藏对象类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "targetId", value = "收藏对象ID", required = true, dataType = "String", paramType = "query")
    })
    public Result<?> delete(@RequestParam(required = false) Long userId,
                            @RequestParam String targetType,
                            @RequestParam String targetId) {
        // 优先从UserContext获取userId（小程序端JWT token）
        Long finalUserId = UserContext.getUserId();
        
        // 如果UserContext中没有，尝试从TokenUtil获取（管理后台token）
        if (finalUserId == null) {
            try {
                SessionUserInfo userInfo = tokenUtil.getUserInfo();
                if (userInfo != null && userInfo.getUserId() > 0) {
                    finalUserId = (long) userInfo.getUserId();
                }
            } catch (Exception e) {
                log.debug("从TokenUtil获取用户信息失败（可能是小程序端或匿名访问）: {}", e.getMessage());
            }
        }
        
        // 如果从token获取失败，使用传入的userId参数
        if (finalUserId == null) {
            finalUserId = userId;
        }
        
        // 如果仍然为null，说明是匿名用户，使用匿名用户ID=0
        if (finalUserId == null) {
            finalUserId = 0L;
            log.debug("匿名用户取消收藏，使用userId=0");
        }
        
        return userCollectService.delete(finalUserId, targetType, targetId);
    }

    @GetMapping("/count")
    @ApiOperation("按对象统计收藏数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "targetType", value = "收藏对象类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "targetId", value = "收藏对象ID", required = true, dataType = "String", paramType = "query")
    })
    public Result<Integer> countByTarget(@RequestParam String targetType,
                                         @RequestParam String targetId) {
        return userCollectService.countByTarget(targetType, targetId);
    }

    @GetMapping("/hotspot")
    @ApiOperation("收藏热点榜（按收藏人数降序，可限定最近N天，可标记当前用户是否收藏）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "targetType", value = "收藏对象类型（可选，不传则查询所有类型）", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "days", value = "限定最近N天（可选，默认7）", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID（可选，传递后会在结果中标记该用户是否收藏）", required = false, dataType = "Long", paramType = "query")
    })
    public Result<PageResult<com.bistu.ecadmin.pojo.UserCollectHotspot>> hotspot(@RequestParam(defaultValue = "1") Integer page,
                                                                                 @RequestParam(defaultValue = "10") Integer limit,
                                                                                 @RequestParam(required = false) String targetType,
                                                                                 @RequestParam(required = false) Integer days,
                                                                                 @RequestParam(required = false) Long userId) {
        Integer d = (days == null || days <= 0) ? 7 : days;
        return userCollectService.hotspot(page, limit, targetType, d, userId);
    }
}

