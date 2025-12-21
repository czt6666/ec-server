package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.DTO.StationPageQueryDTO;
import com.bistu.ecadmin.dao.mapper.StationMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Station;
import com.bistu.ecadmin.service.StationService;
import com.bistu.ecadmin.util.UserContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationMapper stationMapper;

    @Value("${file.export.path}")
    private String exportPath;

    @Override
    public PageResult page(StationPageQueryDTO dto) {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        dto.setUserId(userId);
        
        int pageNum = dto.getPage() == null || dto.getPage() < 1 ? 1 : dto.getPage();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 10 : dto.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<Station> page = stationMapper.pageQuery(dto);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Station getById(Long id) {
        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        return stationMapper.selectById(id, userId);
    }

    @Override
    public boolean add(Station station) {
        // 名称唯一校验
        if (stationMapper.countByName(station.getName(), null) > 0) {
            throw new IllegalArgumentException("驿站名称已存在");
        }
        // subject_type_id 允许为空，若前端未传则设为 0 以避免数据库非空/外键约束
        if (station.getSubjectTypeId() == null) {
            station.setSubjectTypeId(0L);
        }
        station.setCreateTime(LocalDateTime.now());
        return stationMapper.insert(station) > 0;
    }

    @Override
    public boolean update(Station station) {
        // 名称唯一校验（排除自身）
        if (stationMapper.countByName(station.getName(), station.getId()) > 0) {
            throw new IllegalArgumentException("驿站名称已存在");
        }
        return stationMapper.update(station) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return stationMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importStations(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int errorCount = 0;
        List<String> errorMessages = new ArrayList<>();

        try {
            // 验证文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || (!originalFilename.toLowerCase().endsWith(".xlsx") && !originalFilename.toLowerCase().endsWith(".xls"))) {
                throw new RuntimeException("文件格式错误：只支持 .xlsx 或 .xls 格式的 Excel 文件");
            }

            Workbook workbook;
            try {
                workbook = WorkbookFactory.create(file.getInputStream());
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("FileSharingRecord")) {
                    throw new RuntimeException("文件格式错误：请确保上传的是有效的 Excel 文件（.xlsx 或 .xls），不要使用 CSV 文件。CSV 文件需要先在 Excel 中打开并另存为 Excel 格式。");
                }
                throw new RuntimeException("文件解析失败：请确保上传的是有效的 Excel 文件。错误信息：" + e.getMessage());
            }

            if (workbook.getNumberOfSheets() == 0) {
                throw new RuntimeException("Excel 文件中没有工作表");
            }

            Sheet sheet = workbook.getSheetAt(0);

            // 跳过标题行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    Station station = parseStationFromRow(row);
                    if (station != null) {
                        // 检查必填字段
                        if (station.getName() == null || station.getName().trim().isEmpty()) {
                            errorMessages.add("第" + (i + 1) + "行：驿站名称不能为空");
                            errorCount++;
                            continue;
                        }
                        if (station.getRegisteredAddress() == null || station.getRegisteredAddress().trim().isEmpty()) {
                            errorMessages.add("第" + (i + 1) + "行：注册地址不能为空");
                            errorCount++;
                            continue;
                        }
                        if (station.getBusinessAddress() == null || station.getBusinessAddress().trim().isEmpty()) {
                            errorMessages.add("第" + (i + 1) + "行：经营地址不能为空");
                            errorCount++;
                            continue;
                        }

                        // 检查名称是否重复
                        if (stationMapper.countByName(station.getName(), null) > 0) {
                            errorMessages.add("第" + (i + 1) + "行：驿站名称已存在");
                            errorCount++;
                            continue;
                        }

                        // 设置默认值
                        LocalDateTime now = LocalDateTime.now();
                        station.setCreateTime(now);
                        if (station.getSubjectTypeId() == null) {
                            station.setSubjectTypeId(0L);
                        }
                        if (station.getBusinessStatus() == null) {
                            station.setBusinessStatus(1);
                        }

                        stationMapper.insert(station);
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
    public Resource exportStations() {
        try {
            // 获取所有驿站数据
            List<Station> stationList = stationMapper.listAll();

            // 创建导出目录
            File exportDir = new File(exportPath);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            // 生成文件名
            String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String filename = "驿站信息_" + dateStr + ".xlsx";
            String filePath = exportPath + filename;

            // 创建Excel文件
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("驿站信息");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "驿站名称", "注册地址", "经营地址",
                    "统一社会信用代码", "法定代表人", "注册资本(万元)", "成立日期", "营业期限",
                    "官方联系电话", "紧急联系人", "紧急联系电话", "官方邮箱", "主体类型ID", "服务模式",
                    "养老机构设立许可证编号", "医疗机构执业许可证编号", "食品经营许可证编号", "消防验收合格证明编号",
                    "营业状态", "总床数", "房型配置", "护理等级", "价格区间", "驿站简介", "环境照片", "创建时间"
            };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 填充数据
            for (int i = 0; i < stationList.size(); i++) {
                Station station = stationList.get(i);
                Row row = sheet.createRow(i + 1);

                int colIndex = 0;
                row.createCell(colIndex++).setCellValue(station.getName() != null ? station.getName() : "");
                row.createCell(colIndex++).setCellValue(station.getRegisteredAddress() != null ? station.getRegisteredAddress() : "");
                row.createCell(colIndex++).setCellValue(station.getBusinessAddress() != null ? station.getBusinessAddress() : "");
                row.createCell(colIndex++).setCellValue(station.getUnifiedSocialCreditCode() != null ? station.getUnifiedSocialCreditCode() : "");
                row.createCell(colIndex++).setCellValue(station.getLegalRepresentative() != null ? station.getLegalRepresentative() : "");
                row.createCell(colIndex++).setCellValue(station.getRegisteredCapital() != null ? station.getRegisteredCapital().toString() : "");
                row.createCell(colIndex++).setCellValue(station.getEstablishmentDate() != null ? station.getEstablishmentDate().toString() : "");
                row.createCell(colIndex++).setCellValue(station.getBusinessTerm() != null ? station.getBusinessTerm() : "");
                row.createCell(colIndex++).setCellValue(station.getOfficialPhone() != null ? station.getOfficialPhone() : "");
                row.createCell(colIndex++).setCellValue(station.getEmergencyContact() != null ? station.getEmergencyContact() : "");
                row.createCell(colIndex++).setCellValue(station.getEmergencyPhone() != null ? station.getEmergencyPhone() : "");
                row.createCell(colIndex++).setCellValue(station.getOfficialEmail() != null ? station.getOfficialEmail() : "");
                row.createCell(colIndex++).setCellValue(station.getSubjectTypeId() != null ? station.getSubjectTypeId().toString() : "");
                row.createCell(colIndex++).setCellValue(station.getServiceMode() != null ? station.getServiceMode() : "");
                row.createCell(colIndex++).setCellValue(station.getElderlyLicenseNo() != null ? station.getElderlyLicenseNo() : "");
                row.createCell(colIndex++).setCellValue(station.getMedicalLicenseNo() != null ? station.getMedicalLicenseNo() : "");
                row.createCell(colIndex++).setCellValue(station.getFoodLicenseNo() != null ? station.getFoodLicenseNo() : "");
                row.createCell(colIndex++).setCellValue(station.getFireAcceptanceNo() != null ? station.getFireAcceptanceNo() : "");
                row.createCell(colIndex++).setCellValue(station.getBusinessStatus() != null ? station.getBusinessStatus().toString() : "");
                row.createCell(colIndex++).setCellValue(station.getTotalBeds() != null ? station.getTotalBeds() : 0);
                row.createCell(colIndex++).setCellValue(station.getRoomConfig() != null ? station.getRoomConfig() : "");
                row.createCell(colIndex++).setCellValue(station.getCareLevel() != null ? station.getCareLevel() : "");
                row.createCell(colIndex++).setCellValue(station.getPriceRange() != null ? station.getPriceRange() : "");
                row.createCell(colIndex++).setCellValue(station.getIntroduction() != null ? station.getIntroduction() : "");
                row.createCell(colIndex++).setCellValue(station.getEnvironmentPhotos() != null ? station.getEnvironmentPhotos() : "");
                row.createCell(colIndex++).setCellValue(station.getCreateTime() != null ? station.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
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
            return new FileSystemResource(file);

        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    private Station parseStationFromRow(Row row) {
        Station station = new Station();

        try {
            int colIndex = 0;
            // 驿站名称（必填）
            Cell nameCell = row.getCell(colIndex++);
            if (nameCell != null) {
                station.setName(getCellStringValue(nameCell));
            }

            // 注册地址（必填）
            Cell registeredAddressCell = row.getCell(colIndex++);
            if (registeredAddressCell != null) {
                station.setRegisteredAddress(getCellStringValue(registeredAddressCell));
            }

            // 经营地址（必填）
            Cell businessAddressCell = row.getCell(colIndex++);
            if (businessAddressCell != null) {
                station.setBusinessAddress(getCellStringValue(businessAddressCell));
            }

            // 统一社会信用代码
            Cell unifiedSocialCreditCodeCell = row.getCell(colIndex++);
            if (unifiedSocialCreditCodeCell != null) {
                station.setUnifiedSocialCreditCode(getCellStringValue(unifiedSocialCreditCodeCell));
            }

            // 法定代表人
            Cell legalRepresentativeCell = row.getCell(colIndex++);
            if (legalRepresentativeCell != null) {
                station.setLegalRepresentative(getCellStringValue(legalRepresentativeCell));
            }

            // 注册资本
            Cell registeredCapitalCell = row.getCell(colIndex++);
            if (registeredCapitalCell != null) {
                String capitalStr = getCellStringValue(registeredCapitalCell);
                if (capitalStr != null && !capitalStr.trim().isEmpty()) {
                    try {
                        station.setRegisteredCapital(new BigDecimal(capitalStr));
                    } catch (Exception e) {
                        // 忽略转换错误
                    }
                }
            }

            // 成立日期
            Cell establishmentDateCell = row.getCell(colIndex++);
            if (establishmentDateCell != null) {
                String dateStr = getCellStringValue(establishmentDateCell);
                if (dateStr != null && !dateStr.trim().isEmpty()) {
                    try {
                        station.setEstablishmentDate(LocalDate.parse(dateStr));
                    } catch (Exception e) {
                        // 忽略转换错误
                    }
                }
            }

            // 营业期限
            Cell businessTermCell = row.getCell(colIndex++);
            if (businessTermCell != null) {
                station.setBusinessTerm(getCellStringValue(businessTermCell));
            }

            // 官方联系电话
            Cell officialPhoneCell = row.getCell(colIndex++);
            if (officialPhoneCell != null) {
                station.setOfficialPhone(getCellStringValue(officialPhoneCell));
            }

            // 紧急联系人
            Cell emergencyContactCell = row.getCell(colIndex++);
            if (emergencyContactCell != null) {
                station.setEmergencyContact(getCellStringValue(emergencyContactCell));
            }

            // 紧急联系电话
            Cell emergencyPhoneCell = row.getCell(colIndex++);
            if (emergencyPhoneCell != null) {
                station.setEmergencyPhone(getCellStringValue(emergencyPhoneCell));
            }

            // 官方邮箱
            Cell officialEmailCell = row.getCell(colIndex++);
            if (officialEmailCell != null) {
                station.setOfficialEmail(getCellStringValue(officialEmailCell));
            }

            // 主体类型ID
            Cell subjectTypeIdCell = row.getCell(colIndex++);
            if (subjectTypeIdCell != null) {
                String idStr = getCellStringValue(subjectTypeIdCell);
                if (idStr != null && !idStr.trim().isEmpty()) {
                    try {
                        station.setSubjectTypeId(Long.parseLong(idStr));
                    } catch (Exception e) {
                        // 忽略转换错误
                    }
                }
            }

            // 服务模式
            Cell serviceModeCell = row.getCell(colIndex++);
            if (serviceModeCell != null) {
                station.setServiceMode(getCellStringValue(serviceModeCell));
            }

            // 养老机构设立许可证编号
            Cell elderlyLicenseNoCell = row.getCell(colIndex++);
            if (elderlyLicenseNoCell != null) {
                station.setElderlyLicenseNo(getCellStringValue(elderlyLicenseNoCell));
            }

            // 医疗机构执业许可证编号
            Cell medicalLicenseNoCell = row.getCell(colIndex++);
            if (medicalLicenseNoCell != null) {
                station.setMedicalLicenseNo(getCellStringValue(medicalLicenseNoCell));
            }

            // 食品经营许可证编号
            Cell foodLicenseNoCell = row.getCell(colIndex++);
            if (foodLicenseNoCell != null) {
                station.setFoodLicenseNo(getCellStringValue(foodLicenseNoCell));
            }

            // 消防验收合格证明编号
            Cell fireAcceptanceNoCell = row.getCell(colIndex++);
            if (fireAcceptanceNoCell != null) {
                station.setFireAcceptanceNo(getCellStringValue(fireAcceptanceNoCell));
            }

            // 营业状态
            Cell businessStatusCell = row.getCell(colIndex++);
            if (businessStatusCell != null) {
                String statusStr = getCellStringValue(businessStatusCell);
                if (statusStr != null && !statusStr.trim().isEmpty()) {
                    try {
                        station.setBusinessStatus(Integer.parseInt(statusStr));
                    } catch (Exception e) {
                        // 忽略转换错误
                    }
                }
            }

            // 总床数
            Cell totalBedsCell = row.getCell(colIndex++);
            if (totalBedsCell != null) {
                station.setTotalBeds(getCellIntValue(totalBedsCell));
            }

            // 房型配置
            Cell roomConfigCell = row.getCell(colIndex++);
            if (roomConfigCell != null) {
                station.setRoomConfig(getCellStringValue(roomConfigCell));
            }

            // 护理等级
            Cell careLevelCell = row.getCell(colIndex++);
            if (careLevelCell != null) {
                station.setCareLevel(getCellStringValue(careLevelCell));
            }

            // 价格区间
            Cell priceRangeCell = row.getCell(colIndex++);
            if (priceRangeCell != null) {
                station.setPriceRange(getCellStringValue(priceRangeCell));
            }

            // 机构简介
            Cell introductionCell = row.getCell(colIndex++);
            if (introductionCell != null) {
                station.setIntroduction(getCellStringValue(introductionCell));
            }

            // 环境照片
            Cell environmentPhotosCell = row.getCell(colIndex++);
            if (environmentPhotosCell != null) {
                station.setEnvironmentPhotos(getCellStringValue(environmentPhotosCell));
            }

        } catch (Exception e) {
            throw new RuntimeException("解析行数据失败：" + e.getMessage());
        }

        return station;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private Integer getCellIntValue(Cell cell) {
        if (cell == null) return null;
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return (int) cell.getNumericCellValue();
                case STRING:
                    String str = cell.getStringCellValue().trim();
                    return str.isEmpty() ? null : Integer.parseInt(str);
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}

