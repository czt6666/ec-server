package com.bistu.system.log.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.common.config.system.MySqlOpenConfig;
import com.bistu.common.util.CommonUtil;
import com.bistu.system.log.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @author SJJ
 * @description:
 * @date 2022/8/2
 */
@ConditionalOnBean({MySqlOpenConfig.class})
@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {

    @Autowired
    private LogService logService;
    @PostConstruct
    public void test(){
        log.error("logController!!!!!!!");
    }

    /**
     *
     * @method ：获取日志列表
     * @description 将日志列表封装为分页好的Json格式返回前端以供查看及检索
     * @param request 请求参数json
     * @return Json{totalPage，list，totalCount}即{页数，日志列表，目录总数}
     */
    @RequiresPermissions("log:list")
    @GetMapping("/listLog")
    public JSONObject listLog(HttpServletRequest request){
        return logService.listLog(CommonUtil.request2Json(request));
    }

    @GetMapping("/detailLog")
    public JSONObject detailLog(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson, "id");
        return logService.detailLog(requestJson);
    }

    /**
     *
     * @method ：获取操作日志列表
     * @description 将日志列表封装为分页好的Json格式返回前端以供查看及检索
     * @param request 请求参数json
     * @return Json{totalPage，list，totalCount}即{页数，日志列表，目录总数}
     */
    @RequiresPermissions("log:list")
    @GetMapping("/listOperateLog")
    public JSONObject listOperateLog(HttpServletRequest request){
        return logService.listOperateLog(CommonUtil.request2Json(request));
    }

    @GetMapping("/detailOperateLog/{id}")
    public JSONObject detailOperateLog(@PathVariable Long id){
        return logService.detailOperateLog(id);
    }

}
