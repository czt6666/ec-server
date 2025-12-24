package com.bistu.system.log.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.annotation.RequiresPermissions;
import com.bistu.common.config.system.MySqlOpenConfig;
import com.bistu.common.util.CommonUtil;
import com.bistu.system.log.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author SJJ
 * @description:
 * @date 2022/8/2
 */
@Api(tags = "系统日志")
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
    @ApiOperation(value = "日志列表")
    @GetMapping("/listLog")
    public JSONObject listLog(HttpServletRequest request){
        return logService.listLog(CommonUtil.request2Json(request));
    }

    @ApiOperation(value = "日志详情")
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
    @ApiOperation(value = "操作日志列表")
    @GetMapping("/listOperateLog")
    public JSONObject listOperateLog(HttpServletRequest request){
        return logService.listOperateLog(CommonUtil.request2Json(request));
    }

    @ApiOperation(value = "操作日志详情")
    @GetMapping("/detailOperateLog/{id}")
    public JSONObject detailOperateLog(@PathVariable Long id){
        return logService.detailOperateLog(id);
    }

    /**
     * 导出登录日志
     */
    @ApiOperation(value = "导出登录日志")
    @PostMapping("/exportLoginLogs")
    public ResponseEntity<Resource> exportLoginLogs(@RequestBody JSONObject jsonObject) {
        try {
            System.out.println("开始导出登录日志，请求参数: " + jsonObject);
            Resource resource = logService.exportLoginLogs(jsonObject);
            System.out.println("登录日志导出完成，文件名: " + resource.getFilename());
            
            // 设置响应头
            String fileName = resource.getFilename();
            System.out.println("准备返回文件: " + fileName);
            // 确保文件名是URL编码的，特别是对于中文字符
            String encodedFileName = java.net.URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(resource);
        } catch (Exception e) {
            System.err.println("导出登录日志失败: " + e.getMessage());
            e.printStackTrace(); // 打印完整的堆栈跟踪信息
            log.error("导出登录日志失败", e);
            
            // 创建一个包含错误信息的临时资源
            String errorMessage = "{\"msg\":\"导出登录日志失败: " + e.getMessage() + "\",\"code\":500,\"detail\":\"" + e.getClass().getSimpleName() + "\"}";
            InputStream inputStream = new ByteArrayInputStream(errorMessage.getBytes());
            org.springframework.core.io.InputStreamResource errorResource = new org.springframework.core.io.InputStreamResource(inputStream);
            
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=error.json")
                    .body(errorResource);
        }
    }

}