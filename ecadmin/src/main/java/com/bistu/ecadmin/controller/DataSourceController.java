package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.pojo.DataSource;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data-sources")
@CrossOrigin
public class DataSourceController {
    
    @Autowired
    private DataSourceService dataSourceService;
    
    // 获取所有数据源
    @GetMapping
    public Result<List<DataSource>> getAllDataSources() {
        List<DataSource> dataSources = dataSourceService.findAll();
        return Result.success(dataSources);
    }

    // 获取数据源详情
    @GetMapping("/{id}")
    public Result<DataSource> getDataSourceById(@PathVariable Long id) {
        DataSource dataSource = dataSourceService.findById(id);
        if (dataSource != null) {
            return Result.success(dataSource);
        } else {
            return Result.error("数据源不存在");
        }
    }

    // 创建数据源
    @PostMapping
    public Result<String> createDataSource(@RequestBody DataSource dataSource) {
        // 检查必要字段
        if (dataSource.getName() == null || dataSource.getName().isEmpty()) {
            return Result.error("数据源名称不能为空");
        }
        
        if (dataSource.getTableName() == null || dataSource.getTableName().isEmpty()) {
            return Result.error("表名不能为空");
        }
        
        // 简化处理，实际应检查数据源名称是否已存在
        dataSource.setStatus(1); // 默认启用状态
        // 设置默认同步频率为每小时
        if (dataSource.getSyncFrequency() == null || dataSource.getSyncFrequency().isEmpty()) {
            dataSource.setSyncFrequency("0 0 * * * ?"); // 每小时同步
        }
        
        if (dataSourceService.save(dataSource)) {
            return Result.success("数据源创建成功");
        } else {
            return Result.error("数据源创建失败");
        }
    }

    // 更新数据源
    @PutMapping("/{id}")
    public Result<String> updateDataSource(@PathVariable Long id, @RequestBody DataSource dataSource) {
        dataSource.setId(id);
        if (dataSourceService.update(dataSource)) {
            return Result.success("数据源更新成功");
        } else {
            return Result.error("数据源更新失败");
        }
    }

    // 删除数据源
    @DeleteMapping("/{id}")
    public Result<String> deleteDataSource(@PathVariable Long id) {
        if (dataSourceService.deleteById(id)) {
            return Result.success("数据源删除成功");
        } else {
            return Result.error("数据源删除失败");
        }
    }

    // 更新数据源状态
    @PutMapping("/{id}/status")
    public Result<String> updateDataSourceStatus(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Integer status = payload.get("status");
        if (status != null && (status == 0 || status == 1)) {
            if (dataSourceService.updateStatus(id, status)) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } else {
            return Result.error("状态值无效");
        }
    }
    
    // 设置数据源同步频率
    @PutMapping("/{id}/frequency")
    public Result<String> updateDataSourceFrequency(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String frequency = payload.get("frequency");
        if (frequency != null && !frequency.isEmpty()) {
            if (dataSourceService.updateFrequency(id, frequency)) {
                return Result.success("同步频率更新成功");
            } else {
                return Result.error("同步频率更新失败");
            }
        } else {
            return Result.error("同步频率不能为空");
        }
    }

    // 同步所有数据源
    @PostMapping("/sync-all")
    public Result<String> syncAllDataSources() {
        try {
            // 调用同步服务更新所有数据源的记录数
            dataSourceService.syncAllDataSourceCounts();
            return Result.success("所有数据源同步任务已启动");
        } catch (Exception e) {
            return Result.error("同步所有数据源失败: " + e.getMessage());
        }
    }

    // 同步单个数据源
    @PostMapping("/{id}/sync")
    public Result<String> syncDataSource(@PathVariable Long id) {
        DataSource dataSource = dataSourceService.findById(id);
        if (dataSource != null) {
            // 调用同步服务更新记录数
            try {
                dataSourceService.syncDataSourceCount(id);
                return Result.success("数据源 " + dataSource.getName() + " 同步成功");
            } catch (Exception e) {
                return Result.error("数据源同步失败: " + e.getMessage());
            }
        } else {
            return Result.error("数据源不存在");
        }
    }

    // 获取统计数据
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        List<DataSource> dataSources = dataSourceService.findAll();
        long totalSources = dataSources.size();
        long activeSources = dataSources.stream().filter(ds -> ds.getStatus() == 1).count();
        
        // 使用动态查询获取真实记录数
        long totalRecords = dataSources.stream()
            .mapToLong(ds -> {
                if (ds.getTableName() != null && !ds.getTableName().isEmpty()) {
                    return dataSourceService.countByTableName(ds.getTableName());
                }
                return 0L;
            })
            .sum();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSources", totalSources);
        stats.put("activeSources", activeSources);
        stats.put("totalRecords", totalRecords);
        stats.put("systemStatus", "normal"); // 简化处理，实际应检查系统状态
        
        return Result.success(stats);
    }
}