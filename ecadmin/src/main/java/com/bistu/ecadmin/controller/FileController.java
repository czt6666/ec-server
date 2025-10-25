// FileController.java
package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@Api(tags = "文件管理")
@Slf4j
public class FileController {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.access.path}")
    private String accessPath;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;
    @Value("${file.export.path}")
    private String exportPath;

    @Value("${file.export.access.path}")
    private String exportAccessPath;

    @GetMapping("/getConfig")
    @ApiOperation("获取文件配置")
    public Result<Map<String, Object>> getConfig() {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put("uploadPath", uploadPath);
            config.put("accessPath", accessPath);
            config.put("exportPath", exportPath);
            config.put("exportAccessPath", exportAccessPath);
            config.put("maxFileSize", maxFileSize);
            config.put("allowedTypes", new String[]{"jpg", "jpeg", "png", "gif", "pdf", "doc", "docx", "xlsx", "xls"});
            config.put("baseUrl", "http://localhost:8020");
            return Result.success(config);
        } catch (Exception e) {
            log.error("获取文件配置失败", e);
            return Result.error("获取配置失败：" + e.getMessage());
        }
    }

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File destFile = new File(uploadDir, filename);
            file.transferTo(destFile);

            Map<String, Object> result = new HashMap<>();
            result.put("filename", filename);
            result.put("originalName", originalFilename);
            result.put("size", file.getSize());
            result.put("url", accessPath + filename);

            return Result.success(result);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}