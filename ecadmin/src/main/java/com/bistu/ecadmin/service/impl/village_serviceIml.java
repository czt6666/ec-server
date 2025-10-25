package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.DTO.VillagePageQueryDTO;
import com.bistu.ecadmin.dao.mapper.VillageMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.village;
import com.bistu.ecadmin.service.village_service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class village_serviceIml implements village_service {
@Autowired
private VillageMapper villageMapper;

    @Override
    public PageResult page(VillagePageQueryDTO dto) {
        int pageNum = dto.getPage() == null || dto.getPage() < 1 ? 1 : dto.getPage();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 10 : dto.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<village> page = villageMapper.pageQuery(); // 下一条SQL自动分页
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<village> list(village village) {
        return villageMapper.list(village);
    }

    @Override
    public void add(village village) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        village.setCreateTime(now);
        village.setUpdateTime(now);

        // 设置默认管理人数
        if (village.getManagerCount() == null) {
            village.setManagerCount(150);
        }

        villageMapper.insert(village);
    }

    @Override
    public void update(village village) {
        // 设置更新时间
        village.setUpdateTime(LocalDateTime.now());
        villageMapper.update(village);
    }

    @Override
    public village getById(Integer id) {
        return villageMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        village village = villageMapper.getById(id);
        if (village == null) {
            throw new RuntimeException("村庄不存在");
        }

        try {
            // 先删除关联的子表数据
            villageMapper.deleteVillageNewsByVillageName(village.getVillageName());
            villageMapper.deleteVillageHomestayByVillageId(id);

            // 最后删除村庄主记录
            villageMapper.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("删除失败，存在关联数据：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> importVillages(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int errorCount = 0;
        List<String> errorMessages = new ArrayList<>();

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // 跳过标题行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    village village = parseVillageFromRow(row);
                    if (village != null) {
                        // 检查必填字段
                        if (village.getVillageName() == null || village.getVillageName().trim().isEmpty()) {
                            errorMessages.add("第" + (i + 1) + "行：村庄名称不能为空");
                            errorCount++;
                            continue;
                        }
                        if (village.getAddress() == null || village.getAddress().trim().isEmpty()) {
                            errorMessages.add("第" + (i + 1) + "行：详细地址不能为空");
                            errorCount++;
                            continue;
                        }

                        // 设置默认值
                        LocalDateTime now = LocalDateTime.now();
                        village.setCreateTime(now);
                        village.setUpdateTime(now);
                        if (village.getManagerCount() == null) {
                            village.setManagerCount(150);
                        }

                        villageMapper.insert(village);
                        successCount++;
                    }
                } catch (Exception e) {
                    errorMessages.add("第" + (i + 1) + "行：" + e.getMessage());
                    errorCount++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("文件解析失败：" + e.getMessage());
        }

        result.put("successCount", successCount);
        result.put("errorCount", errorCount);
        result.put("errorMessages", errorMessages);
        return result;
    }

    @Override
    public ByteArrayResource getTemplateFile() {
        try {
            // 创建Excel模板
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("村庄信息");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "村庄名称*", "详细地址*", "村庄描述", "村书记姓名", "联系方式",
                    "户数", "管理人数", "总面积(亩)", "耕地面积(亩)", "林地面积(亩)",
                    "水域面积(亩)", "建设用地面积(亩)"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 设置列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入示例数据
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue("示例村庄");
            exampleRow.createCell(1).setCellValue("浙江省杭州市余杭区良渚街道1号");
            exampleRow.createCell(2).setCellValue("美丽的乡村");
            exampleRow.createCell(3).setCellValue("张三");
            exampleRow.createCell(4).setCellValue("13800000001");
            exampleRow.createCell(5).setCellValue(100);
            exampleRow.createCell(6).setCellValue(150);
            exampleRow.createCell(7).setCellValue(1000.5);
            exampleRow.createCell(8).setCellValue(500.2);
            exampleRow.createCell(9).setCellValue(300.3);
            exampleRow.createCell(10).setCellValue(200.0);
            exampleRow.createCell(11).setCellValue(100.0);

            // 转换为字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            byte[] bytes = outputStream.toByteArray();
            return new org.springframework.core.io.ByteArrayResource(bytes);

        } catch (Exception e) {
            throw new RuntimeException("生成模板文件失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> checkDeleteConstraints(Integer id) {
        Map<String, Object> result = new HashMap<>();
        village village = villageMapper.getById(id);

        if (village == null) {
            result.put("canDelete", false);
            result.put("message", "村庄不存在");
            return result;
        }

        // 检查关联数据
        int newsCount = villageMapper.countVillageNewsByVillageName(village.getVillageName());
        int homestayCount = villageMapper.countVillageHomestayByVillageId(id);

        List<String> constraints = new ArrayList<>();
        if (newsCount > 0) {
            constraints.add("村庄新闻(" + newsCount + "条)");
        }
        if (homestayCount > 0) {
            constraints.add("村庄民宿(" + homestayCount + "条)");
        }

        if (constraints.isEmpty()) {
            result.put("canDelete", true);
            result.put("message", "可以安全删除");
        } else {
            result.put("canDelete", false);
            result.put("message", "存在关联数据：" + String.join("、", constraints));
            result.put("constraints", constraints);
        }

        return result;
    }

    @Override
    public void deleteWithCascade(Integer id) {
        village village = villageMapper.getById(id);
        if (village == null) {
            throw new RuntimeException("村庄不存在");
        }

        try {
            // 先删除关联的子表数据
            villageMapper.deleteVillageNewsByVillageName(village.getVillageName());
            villageMapper.deleteVillageHomestayByVillageId(id);

            // 最后删除村庄主记录
            villageMapper.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    private village parseVillageFromRow(Row row) {
        village village = new village();

        try {
            // 村庄名称（必填）
            Cell villageNameCell = row.getCell(0);
            if (villageNameCell != null) {
                village.setVillageName(getCellStringValue(villageNameCell));
            }

            // 详细地址（必填）
            Cell addressCell = row.getCell(1);
            if (addressCell != null) {
                village.setAddress(getCellStringValue(addressCell));
            }

            // 村庄描述
            Cell descriptionCell = row.getCell(2);
            if (descriptionCell != null) {
                village.setVillageDescription(getCellStringValue(descriptionCell));
            }

            // 村书记姓名
            Cell secretaryNameCell = row.getCell(3);
            if (secretaryNameCell != null) {
                village.setSecretaryName(getCellStringValue(secretaryNameCell));
            }

            // 联系方式
            Cell phoneCell = row.getCell(4);
            if (phoneCell != null) {
                village.setSecretaryPhone(getCellStringValue(phoneCell));
            }

            // 户数
            Cell householdCell = row.getCell(5);
            if (householdCell != null) {
                village.setHouseholdCount(getCellIntValue(householdCell));
            }

            // 管理人数
            Cell managerCell = row.getCell(6);
            if (managerCell != null) {
                village.setManagerCount(getCellIntValue(managerCell));
            }

            // 总面积
            Cell totalAreaCell = row.getCell(7);
            if (totalAreaCell != null) {
                village.setTotalArea(getCellDoubleValue(totalAreaCell));

            }

            // 耕地面积
            Cell farmlandCell = row.getCell(8);
            if (farmlandCell != null) {
                village.setFarmlandArea(getCellDoubleValue(farmlandCell));
            }

            // 林地面积
            Cell forestCell = row.getCell(9);
            if (forestCell != null) {
                village.setForestArea(getCellDoubleValue(forestCell));
            }

            // 水域面积
            Cell waterCell = row.getCell(10);
            if (waterCell != null) {
                village.setWaterArea(getCellDoubleValue(waterCell));
            }

            // 建设用地面积
            Cell constructionCell = row.getCell(11);
            if (constructionCell != null) {
                village.setConstructionArea(getCellDoubleValue(constructionCell));
            }

        } catch (Exception e) {
            throw new RuntimeException("解析行数据失败：" + e.getMessage());
        }

        return village;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private Integer getCellIntValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    private BigDecimal getCellDoubleValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    return BigDecimal.valueOf(Double.parseDouble(cell.getStringCellValue().trim()));
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }
}

