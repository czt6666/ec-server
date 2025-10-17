package com.bistu.ecadmin.controller;

import com.bistu.ecadmin.dao.DTO.VillagePageQueryDTO;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.village;
import com.bistu.ecadmin.service.iml.village_serviceIml;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "村庄相关接口")
@RequestMapping("/bistu/ecadmin/village")

public class village_controller {
    @Autowired
    private village_serviceIml villageServiceIml;
    @GetMapping("/list")
    @ApiOperation("按条件简单列表（不分页）")
    public Result village_list(village v){

        List<village> list = villageServiceIml.list(v);
        return Result.success(list);

    }
    @ApiOperation("分页查询村庄")
    @GetMapping("/page")
    public Result<PageResult> page(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        VillagePageQueryDTO dto = new VillagePageQueryDTO();
        dto.setPage(page);
        dto.setPageSize(pageSize);
        // 其他条件不再接收，默认不填
        return Result.success(villageServiceIml.page(dto));
    }
    /**
     * 新增村庄
     */
    @ApiOperation("增加村庄村庄")
    @PostMapping("/add")
    public Result<String> add(@RequestBody village village) {
        try {
            villageServiceIml.add(village);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.error("添加失败：" + e.getMessage());
        }
    }
    // 在 VillageController 中添加
    /**
     * 修改村庄
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody village village) {
        try {
            villageServiceIml.update(village);
            return Result.success("修改成功");
        } catch (Exception e) {
            return Result.error("修改失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询村庄详情
     */
    @GetMapping("/{id}")
    public Result<village> getById(@PathVariable Integer id) {
        try {
            village village = villageServiceIml.getById(id);
            return Result.success(village);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
// 在 VillageController 中添加
    /**
     * 删除村庄
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Integer id) {
        try {
            villageServiceIml.delete(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除村庄
     */

}
