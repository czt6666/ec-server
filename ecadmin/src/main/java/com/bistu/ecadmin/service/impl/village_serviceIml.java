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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class village_serviceIml implements village_service {
@Autowired
private VillageMapper villageMapper;
@Value("${file.export.path}")
private String exportPath;

@Value("${file.export.access.path}")
private String exportAccessPath;

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
        if (village.getVillageName() == null || village.getVillageName().trim().isEmpty()) {
            throw new RuntimeException("村庄名称不能为空");
        }
        int exists = villageMapper.countVillageByName(village.getVillageName().trim(), null);
        if (exists > 0) {
            throw new RuntimeException("村庄名称已存在");
        }

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
        if (village.getVillageName() != null && !village.getVillageName().trim().isEmpty()) {
            int exists = villageMapper.countVillageByName(village.getVillageName().trim(), village.getId());
            if (exists > 0) {
                throw new RuntimeException("村庄名称已存在");
            }
        }
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
            villageMapper.deleteVillageNewsByVillageId(id);
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
    public Resource exportVillages() {
        try {


            // 获取所有村庄数据
            List<village> villageList = villageMapper.list(new village());

            // 创建导出目录
            File exportDir = new File(exportPath);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            // 生成文件名
            String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String filename = "村庄信息_" + dateStr + ".xlsx";
            String filePath = exportPath + filename;

            // 创建Excel文件
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("村庄信息");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "村庄名称", "详细地址", "村庄描述", "村书记姓名", "联系方式",
                    "户数", "管理人数", "总面积(亩)", "耕地面积(亩)", "林地面积(亩)",
                    "水域面积(亩)", "建设用地面积(亩)","创建时间", "更新时间"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据
            for (int i = 0; i < villageList.size(); i++) {
                village village = villageList.get(i);
                Row row = sheet.createRow(i + 1);


                row.createCell(0).setCellValue(village.getVillageName() != null ? village.getVillageName() : "");
                row.createCell(1).setCellValue(village.getAddress() != null ? village.getAddress() : "");
                row.createCell(2).setCellValue(village.getVillageDescription() != null ? village.getVillageDescription() : "");
                row.createCell(3).setCellValue(village.getSecretaryName() != null ? village.getSecretaryName() : "");
                row.createCell(4).setCellValue(village.getSecretaryPhone() != null ? village.getSecretaryPhone() : "");
                row.createCell(5).setCellValue(village.getHouseholdCount() != null ? village.getHouseholdCount() : 0);
                row.createCell(6).setCellValue(village.getManagerCount() != null ? village.getManagerCount() : 0);
                row.createCell(7).setCellValue(String.valueOf(village.getTotalArea() != null ? village.getTotalArea() : 0.0));
                row.createCell(8).setCellValue(String.valueOf(village.getFarmlandArea() != null ? village.getFarmlandArea() : 0.0));
                row.createCell(9).setCellValue(String.valueOf(village.getForestArea() != null ? village.getForestArea() : 0.0));
                row.createCell(10).setCellValue(String.valueOf(village.getWaterArea() != null ? village.getWaterArea() : 0.0));
                row.createCell(11).setCellValue(String.valueOf(village.getConstructionArea() != null ? village.getConstructionArea() : 0.0));
                row.createCell(12).setCellValue(village.getCreateTime() != null ? village.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
                row.createCell(13).setCellValue(village.getUpdateTime() != null ? village.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 保存到文件
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();



            // 返回文件资源
            File file = new File(filePath);
            return new org.springframework.core.io.FileSystemResource(file);

        } catch (Exception e) {

            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    // 辅助方法：安全设置单元格值
    private void setCellValue(Row row, int columnIndex, Object value) {
        Cell cell = row.createCell(columnIndex);
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).toString());
        } else {
            cell.setCellValue(value.toString());
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
        int newsCount = villageMapper.countVillageNewsByVillageId(id);
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
            villageMapper.deleteVillageNewsByVillageId(id);
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

