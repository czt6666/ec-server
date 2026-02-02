package com.bistu.ecadmin.service;

import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.TokenUtil;
import com.bistu.ecadmin.dao.StudyTourPlanDao;
import com.bistu.ecadmin.dao.StudyTourBaseDao;
import com.bistu.ecadmin.pojo.StudyTourPlan;
import com.bistu.ecadmin.pojo.StudyTourBase;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class StudyTourPlanService {

    @Autowired
    private StudyTourPlanDao studyTourPlanDao;

    @Autowired
    private StudyTourBaseDao studyTourBaseDao;

    @Autowired
    private TokenUtil tokenUtil;

    /**
     * 新增研学方案
     */
    public Result createStudyTourPlan(StudyTourPlan studyTourPlan) {
        // 获取当前登录用户信息
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            log.debug("未获取到管理后台token: {}", e.getMessage());
        }

        if (userInfo != null) {
            // 判断是否为管理员
            List<Integer> roleIds = userInfo.getRoleIds();
            boolean isAdmin = (userInfo.getUserId() == 10011)
                    || (roleIds != null && roleIds.contains(1));

            // 验证基地是否存在，并检查权限
            if (studyTourPlan.getBaseId() != null) {
                StudyTourBase base = studyTourBaseDao.selectById(studyTourPlan.getBaseId());
                if (base == null) {
                    return Result.error("关联的研学基地不存在");
                }
                
                if (!isAdmin) {
                    // 商家用户：只能在自己基地下创建方案
                    if (base.getUserId() == null || !base.getUserId().equals((long) userInfo.getUserId())) {
                        throw new IllegalArgumentException("无权在其他基地下创建研学方案");
                    }
                    // 普通商户创建的方案默认处于禁用/下架状态
                    studyTourPlan.setStatus(0);
                } else {
                    // 管理员未填写状态时，默认启用
                    if (studyTourPlan.getStatus() == null) {
                        studyTourPlan.setStatus(1);
                    }
                }
            } else {
                throw new IllegalArgumentException("必须关联一个研学基地");
            }
        } else {
            throw new IllegalArgumentException("未登录，无法新增研学方案");
        }

        try {
            studyTourPlanDao.insert(studyTourPlan);
            // 返回包含id的结果，以便前端处理图片
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("id", studyTourPlan.getId());
            return Result.success(data, "新增成功");
        } catch (Exception e) {
            log.error("新增研学方案失败", e);
            return Result.error("新增失败");
        }
    }

    /**
     * 研学方案分页查询
     */
    public Result<PageResult<StudyTourPlan>> listStudyTourPlans(String planName, Long baseId, Integer status, int page, int pageSize) {
        try {
            // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
            Long userId = UserContext.getUserId();
            
            // 根据当前登录用户限制可见方案：
            // - 管理员(userId=10011 或 roleId 包含 1)：查看全部方案
            // - 普通商家用户：仅查看自己基地下的方案
            
            SessionUserInfo userInfo = null;
            Long merchantUserId = null;
            try {
                userInfo = tokenUtil.getUserInfo();
            } catch (Exception e) {
                log.debug("未获取到管理后台token（可能是小程序端或匿名访问）: {}", e.getMessage());
            }

            // 如果从token获取到用户信息，进行权限过滤（后台管理端）
            if (userInfo != null) {
                List<Integer> roleIds = userInfo.getRoleIds();
                boolean isAdmin = (userInfo.getUserId() == 10011)
                        || (roleIds != null && roleIds.contains(1));
                if (!isAdmin) {
                    // 商家用户只能查看自己基地下的方案
                    merchantUserId = (long) userInfo.getUserId();
                }
            } else {
                // 小程序端 / 匿名访问：如果未显式传入 status，则只展示启用的方案（status = 1）
                if (status == null) {
                    status = 1;
                }
            }
            
            PageHelper.startPage(page, pageSize);
            List<StudyTourPlan> studyTourPlanList = studyTourPlanDao.list(planName, baseId, status, merchantUserId, userId);
            PageInfo<StudyTourPlan> pageInfo = new PageInfo<>(studyTourPlanList);
            PageResult<StudyTourPlan> pageResult = new PageResult<>();
            pageResult.setRecords(pageInfo.getList());
            pageResult.setTotal(pageInfo.getTotal());
            return Result.success(pageResult);
        } catch (Exception e) {
            log.error("查询研学方案失败", e);
            return Result.error("查询失败");
        }
    }

    /**
     * 根据id查询研学方案
     */
    public StudyTourPlan getById(Long id, Long userId) {
        try {
            return studyTourPlanDao.getById(id, userId);
        } catch (Exception e) {
            log.error("查询研学方案失败", e);
            return null;
        }
    }

    /**
     * 修改研学方案
     */
    public Result updateStudyTourPlan(StudyTourPlan studyTourPlan) {
        // 权限校验：商家只能修改自己基地下的方案
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法更新研学方案");
        }

        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        StudyTourPlan existing = studyTourPlanDao.getById(studyTourPlan.getId(), userId);
        if (existing == null) {
            return Result.error("研学方案不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能修改自己基地下的方案
            if (existing.getBaseId() != null) {
                StudyTourBase base = studyTourBaseDao.selectById(existing.getBaseId());
                if (base == null || base.getUserId() == null || !base.getUserId().equals((long) userInfo.getUserId())) {
                    throw new IllegalArgumentException("无权修改其他基地的研学方案");
                }
            } else {
                throw new IllegalArgumentException("方案未关联基地，无法验证权限");
            }
            
            // 商家用户不能修改基地关联（如果尝试修改）
            if (studyTourPlan.getBaseId() != null && !studyTourPlan.getBaseId().equals(existing.getBaseId())) {
                throw new IllegalArgumentException("无权修改方案所属基地");
            }

            // 商家用户不能修改方案状态（上下架仅管理员可控）
            studyTourPlan.setStatus(existing.getStatus());
        }

        try {
            studyTourPlanDao.update(studyTourPlan);
            return Result.success("修改成功");
        } catch (Exception e) {
            log.error("修改研学方案失败", e);
            return Result.error("修改失败");
        }
    }



    /**
     * 查询所有启用的研学方案（用于下拉选择）
     */
    public Result<List<StudyTourPlan>> listAllEnabled() {
        try {
            // 根据当前登录用户限制可见方案（用于后台下拉选择）：
            // - 管理员(userId=10011 或 roleId 包含 1)：查看所有已启用方案
            // - 普通商家用户：仅查看自己基地下的已启用方案
            Long merchantUserId = null;
            try {
                SessionUserInfo userInfo = tokenUtil.getUserInfo();
                if (userInfo != null) {
                    List<Integer> roleIds = userInfo.getRoleIds();
                    boolean isAdmin = (userInfo.getUserId() == 10011)
                            || (roleIds != null && roleIds.contains(1));
                    if (!isAdmin) {
                        merchantUserId = (long) userInfo.getUserId();
                    }
                }
            } catch (Exception e) {
                // 未获取到管理后台token（可能是小程序端或匿名访问），不做商户过滤
                log.debug("未获取到管理后台token，listAllEnabled 不进行商户过滤: {}", e.getMessage());
            }

            List<StudyTourPlan> studyTourPlanList = studyTourPlanDao.listAllEnabled(merchantUserId);
            return Result.success(studyTourPlanList);
        } catch (Exception e) {
            log.error("查询启用的研学方案失败", e);
            return Result.error("查询失败");
        }
    }

    /**
     * 上架研学方案（仅管理员，将状态改为1-启用）
     */
    public boolean publish(Long id) {
        SessionUserInfo userInfo;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法上架研学方案");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以上架研学方案");
        }

        StudyTourPlan plan = new StudyTourPlan();
        plan.setId(id);
        plan.setStatus(1); // 1-启用（上架）
        studyTourPlanDao.update(plan);
        return true;
    }

    /**
     * 下架研学方案（仅管理员，将状态改为0-禁用）
     */
    public boolean unpublish(Long id) {
        SessionUserInfo userInfo;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法下架研学方案");
        }

        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));
        if (!isAdmin) {
            throw new IllegalArgumentException("只有管理员可以下架研学方案");
        }

        StudyTourPlan plan = new StudyTourPlan();
        plan.setId(id);
        plan.setStatus(0); // 0-禁用（下架）
        studyTourPlanDao.update(plan);
        return true;
    }
    
    /**
     * 上传方案图片
     */
    public String uploadImage(MultipartFile file) throws Exception {
        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.matches("(?i).*\\.(jpg|jpeg|png)$")) {
            throw new IllegalArgumentException("只支持JPG、PNG格式的图片");
        }
        
        // 验证文件大小
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new IllegalArgumentException("图片大小不能超过5MB");
        }
        
        // 生成文件名
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        
        // 上传路径
        String uploadPath = "D:/ecadmin/upload/images/"; // 实际部署时需要修改
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 保存文件
        File dest = new File(uploadPath + fileName);
        file.transferTo(dest);
        
        // 返回相对路径
        return "/upload/images/" + fileName;
    }
    
    /**
     * 获取方案的图片列表
     */
    public List<Map<String, Object>> getImagesByPlanId(Long planId) {
        return studyTourPlanDao.getImagesByPlanId(planId);
    }
    
    /**
     * 保存方案图片
     */
    public void saveImages(Long planId, List<Map<String, Object>> images) {
        // 先删除旧的图片
        // 1. 先获取旧图片的URL，以便删除真实文件
        List<Map<String, Object>> oldImages = studyTourPlanDao.getImagesByPlanId(planId);
        
        // 2. 删除数据库中的旧图片
        studyTourPlanDao.deleteImagesByPlanId(planId);
        
        // 3. 删除真实的图片文件
        for (Map<String, Object> oldImage : oldImages) {
            String imageUrl = (String) oldImage.get("imageUrl");
            if (imageUrl != null) {
                // 提取文件名并删除文件
                try {
                    // 从URL中提取文件名
                    String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    String filePath = "D:/ecadmin/upload/images/" + fileName;
                    File file = new File(filePath);
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception e) {
                    log.error("删除旧图片文件失败: " + imageUrl, e);
                }
            }
        }
        
        // 保存新的图片
        for (int i = 0; i < images.size(); i++) {
            Map<String, Object> image = images.get(i);
            Map<String, Object> imageData = new java.util.HashMap<>();
            imageData.put("relatedId", planId);
            imageData.put("relatedType", "plan");
            
            // 尝试多种可能的字段名
            Object imageUrlObj = image.get("imageUrl");
            if (imageUrlObj == null) {
                imageUrlObj = image.get("url");
            }
            imageData.put("imageUrl", imageUrlObj);
            
            Object imageNameObj = image.get("imageName");
            if (imageNameObj == null) {
                imageNameObj = image.get("name");
            }
            imageData.put("imageName", imageNameObj);
            
            Object sortOrderObj = image.get("sortOrder");
            if (sortOrderObj == null) {
                sortOrderObj = i;
            }
            imageData.put("sortOrder", sortOrderObj);
            
            Object isCoverObj = image.get("isCover");
            if (isCoverObj == null) {
                isCoverObj = (i == 0 ? 1 : 0); // 默认第一张为封面图
            }
            imageData.put("isCover", isCoverObj);
            
            // 确保所有图片都保存到数据库
            studyTourPlanDao.insertImage(imageData);
        }
    }
    
    /**
     * 删除方案图片
     */
    public boolean deleteImage(Long imageId) {
        studyTourPlanDao.deleteImage(imageId);
        return true;
    }
    
    /**
     * 设置封面图
     */
    public boolean setCoverImage(Long imageId) {
        studyTourPlanDao.setCoverImage(imageId);
        return true;
    }
    
    /**
     * 更新图片排序
     */
    public boolean updateImageSort(List<Map<String, Object>> images) {
        for (int i = 0; i < images.size(); i++) {
            Map<String, Object> image = images.get(i);
            Map<String, Object> sortData = new java.util.HashMap<>();
            sortData.put("id", image.get("id"));
            sortData.put("sortOrder", i);
            
            studyTourPlanDao.updateImageSort(sortData);
        }
        return true;
    }
    
    /**
     * 确保删除方案时同时删除关联的图片
     */
    public Result deleteStudyTourPlan(Long id) {
        // 权限校验：商家只能删除自己基地下的方案
        SessionUserInfo userInfo = null;
        try {
            userInfo = tokenUtil.getUserInfo();
        } catch (Exception e) {
            throw new IllegalArgumentException("未登录，无法删除研学方案");
        }

        // 从 UserContext 获取小程序用户ID（用于判断是否收藏）
        Long userId = UserContext.getUserId();
        StudyTourPlan existing = studyTourPlanDao.getById(id, userId);
        if (existing == null) {
            return Result.error("研学方案不存在");
        }

        // 判断是否为管理员
        List<Integer> roleIds = userInfo.getRoleIds();
        boolean isAdmin = (userInfo.getUserId() == 10011)
                || (roleIds != null && roleIds.contains(1));

        if (!isAdmin) {
            // 商家用户：只能删除自己基地下的方案
            if (existing.getBaseId() != null) {
                StudyTourBase base = studyTourBaseDao.selectById(existing.getBaseId());
                if (base == null || base.getUserId() == null || !base.getUserId().equals((long) userInfo.getUserId())) {
                    throw new IllegalArgumentException("无权删除其他基地的研学方案");
                }
            } else {
                throw new IllegalArgumentException("方案未关联基地，无法验证权限");
            }
        }

        try {
            // 先删除关联的图片
            studyTourPlanDao.deleteImagesByPlanId(id);
            
            // 再删除方案
            studyTourPlanDao.deleteById(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除研学方案失败", e);
            return Result.error("删除失败");
        }
    }
}