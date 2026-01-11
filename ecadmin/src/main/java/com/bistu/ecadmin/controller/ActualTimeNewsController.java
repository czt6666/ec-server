package com.bistu.ecadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.util.CommonUtil;
import com.bistu.ecadmin.annotation.OperateLog;
import com.bistu.ecadmin.service.ActualTimeNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/village/actualtime")
@Slf4j
@Api(tags = "实时新闻管理")
public class ActualTimeNewsController {

    @Autowired
    private ActualTimeNewsService actualTimeNewsService;
    
    // 从配置文件中读取图片存储路径
    @Value("${file.upload.path:/Users/yitis/Projects/ec-server/ecadmin/uploads/}")
    private String uploadPath;
    
    // 从配置文件中读取图片访问路径前缀
    @Value("${file.access.path:/uploads/}")
    private String accessPath;

    /**
     * 上传图片
     */
    @PostMapping(value = "/uploadImage", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation("上传实时新闻图片")
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        // 确保始终有data字段
        Map<String, Object> data = new HashMap<>();
        
        if (file.isEmpty()) {
            response.put("code", 400);
            response.put("msg", "上传文件为空");
            response.put("data", data);
            return response;
        }
        
        try {
            // 获取文件原始名称
            String originalFilename = file.getOriginalFilename();
            
            // 检查是否为null
            if (originalFilename == null || originalFilename.lastIndexOf(".") == -1) {
                response.put("code", 400);
                response.put("msg", "文件格式不正确");
                response.put("data", data);
                return response;
            }
            
            // 获取文件后缀名
            String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            // 生成新的文件名，避免重复
            String newFileName = UUID.randomUUID().toString().replace("-", "") + fileSuffix;
            
            // 不再按日期创建文件夹，直接使用上传路径
            String datePath = "";
            
            // 创建文件夹（如果不存在）
            File folder = new File(uploadPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            
            // 文件存储路径
            String filePath = uploadPath + newFileName;
            
            // 保存文件
            file.transferTo(new File(filePath));
            
            // 返回访问路径
            String fileAccessUrl = accessPath + newFileName;
            
            // 构造前端期望的返回格式
            data.put("url", fileAccessUrl);
            data.put("fileName", newFileName);
            
            response.put("code", 200);
            response.put("msg", "上传成功");
            response.put("data", data);
            
            // 添加日志以便调试
            log.info("文件上传成功: {}, 访问路径: {}", newFileName, fileAccessUrl);
            
            return response;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            response.put("code", 500);
            response.put("msg", "文件上传失败: " + e.getMessage());
            response.put("data", data);
            return response;
        } catch (Exception e) {
            log.error("文件上传异常", e);
            response.put("code", 500);
            response.put("msg", "文件上传异常: " + e.getMessage());
            response.put("data", data);
            return response;
        }
    }

    /**
     * 新增实时新闻
     */
    @PostMapping(value = "/add", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @ApiOperation("新增实时新闻")
    @OperateLog(operation = "新增实时新闻")
    public JSONObject addActualTimeNews(@RequestBody(required = false) JSONObject requestJson, HttpServletRequest request) {
        // 如果没有JSON请求体，则从请求参数中获取
        if (requestJson == null) {
            requestJson = CommonUtil.request2Json(request);
        }
        
        // 处理前端可能传递的不同字段名
        if (requestJson.containsKey("theme_id") && !requestJson.containsKey("themeName")) {
            requestJson.put("themeName", requestJson.getString("theme_id"));
        }
        
        if (requestJson.containsKey("themeid") && !requestJson.containsKey("themeName")) {
            requestJson.put("themeName", requestJson.getString("themeid"));
        }
        
        if (requestJson.containsKey("village_id") && !requestJson.containsKey("villageName")) {
            requestJson.put("villageName", requestJson.getString("village_id"));
        }
        
        if (requestJson.containsKey("villageid") && !requestJson.containsKey("villageName")) {
            requestJson.put("villageName", requestJson.getString("villageid"));
        }
        
        if (requestJson.containsKey("author_id") && !requestJson.containsKey("author")) {
            requestJson.put("author", requestJson.getString("author_id"));
        }
        
        if (requestJson.containsKey("image_url") && !requestJson.containsKey("imageUrl")) {
            requestJson.put("imageUrl", requestJson.getString("image_url"));
        }
        
        // 处理发布状态字段，默认为1（草稿）
        if (!requestJson.containsKey("publish_status")) {
            if (requestJson.containsKey("publishStatus")) {
                requestJson.put("publish_status", requestJson.getInteger("publishStatus"));
            } else {
                // 设置默认值
                requestJson.put("publish_status", 1);
            }
        }
        
        CommonUtil.hasAllRequired(requestJson, "title, author, villageName, themeName, content");
        return actualTimeNewsService.addActualTimeNews(requestJson);
    }

    /**
     * 更新实时新闻
     */
    @PostMapping(value = "/update", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @ApiOperation("更新实时新闻")
    @OperateLog(operation = "更新实时新闻")
    public JSONObject updateActualTimeNews(@RequestBody(required = false) JSONObject requestJson, HttpServletRequest request) {
        // 如果没有JSON请求体，则从请求参数中获取
        if (requestJson == null) {
            requestJson = CommonUtil.request2Json(request);
        }
        
        // 处理前端可能传递的不同字段名
        if (requestJson.containsKey("theme_id") && !requestJson.containsKey("themeName")) {
            requestJson.put("themeName", requestJson.getString("theme_id"));
        }
        
        if (requestJson.containsKey("themeid") && !requestJson.containsKey("themeName")) {
            requestJson.put("themeName", requestJson.getString("themeid"));
        }
        
        if (requestJson.containsKey("village_id") && !requestJson.containsKey("villageName")) {
            requestJson.put("villageName", requestJson.getString("village_id"));
        }
        
        if (requestJson.containsKey("villageid") && !requestJson.containsKey("villageName")) {
            requestJson.put("villageName", requestJson.getString("villageid"));
        }
        
        if (requestJson.containsKey("author_id") && !requestJson.containsKey("author")) {
            requestJson.put("author", requestJson.getString("author_id"));
        }
        
        if (requestJson.containsKey("image_url") && !requestJson.containsKey("imageUrl")) {
            requestJson.put("imageUrl", requestJson.getString("image_url"));
        }
        
        // 处理发布状态字段
        if (requestJson.containsKey("publishStatus") && !requestJson.containsKey("publish_status")) {
            requestJson.put("publish_status", requestJson.getInteger("publishStatus"));
        }
        
        CommonUtil.hasAllRequired(requestJson, "id, title, author, villageName, themeName, content");
        return actualTimeNewsService.updateActualTimeNews(requestJson);
    }

    /**
     * 删除实时新闻
     */
    @PostMapping(value = "/delete", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @ApiOperation("删除实时新闻")
    @OperateLog(operation = "删除实时新闻")
    public JSONObject deleteActualTimeNews(@RequestBody(required = false) JSONObject requestJson, HttpServletRequest request) {
        // 如果没有JSON请求体，则从请求参数中获取
        if (requestJson == null) {
            requestJson = CommonUtil.request2Json(request);
        }
        
        CommonUtil.hasAllRequired(requestJson, "id");
        return actualTimeNewsService.deleteActualTimeNews(requestJson);
    }

    /**
     * 分页查询实时新闻列表
     */
    @GetMapping("/list")
    @ApiOperation("分页查询实时新闻列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "pageRow", value = "每页数量", defaultValue = "10", dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "keyword", value = "搜索关键词", defaultValue = "", dataType = "String", paramType = "query")
    })
    public JSONObject getActualTimeNewsList(HttpServletRequest request) {
        JSONObject requestJson = CommonUtil.request2Json(request);
        
        // 设置默认值
        if (!requestJson.containsKey("pageNum")) {
            requestJson.put("pageNum", 1);
        }
        if (!requestJson.containsKey("pageRow")) {
            requestJson.put("pageRow", 10);
        }
        if (!requestJson.containsKey("keyword")) {
            requestJson.put("keyword", "");
        }
        
        return actualTimeNewsService.getActualTimeNewsList(requestJson);
    }

    /**
     * 根据ID查询实时新闻
     */
    @GetMapping("/detail")
    @ApiOperation("根据ID查询实时新闻详情")
    @ApiImplicitParam(name = "id", value = "新闻ID", required = true, dataType = "Integer", paramType = "query")
    public JSONObject getActualTimeNewsById(@RequestParam(value = "id", defaultValue = "0") Integer id) {
        // 添加默认值检查，避免空值导致的问题
        if (id == null || id <= 0) {
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("code", 400);
            errorResponse.put("msg", "无效的新闻ID");
            return errorResponse;
        }
        return actualTimeNewsService.getActualTimeNewsById(id);
    }
}