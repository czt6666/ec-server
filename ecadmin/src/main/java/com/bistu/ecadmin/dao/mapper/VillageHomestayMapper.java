package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.dao.DTO.VillageHomestayPageQueryDTO;
import com.bistu.ecadmin.dao.DTO.SortRequest;
import com.bistu.ecadmin.pojo.VillageHomestay;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VillageHomestayMapper {

    /**
     * 分页查询民宿列表（支持模糊查询）
     */
    Page<VillageHomestay> pageQuery(VillageHomestayPageQueryDTO dto);

    /**
     * 根据ID查询民宿详情
     */
    VillageHomestay selectById(@Param("id") Integer id, @Param("userId") Long userId);

    /**
     * 新增民宿
     */
    int insert(VillageHomestay homestay);

    /**
     * 更新民宿
     */
    int update(VillageHomestay homestay);

    /**
     * 删除民宿
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 根据乡村ID查询民宿列表
     * @param villageId 乡村ID
     * @param userId 小程序用户ID（用于判断是否收藏，可为null）
     * @param status 状态过滤（小程序端传1-已上架，管理端可传null查看全部）
     */
    List<VillageHomestay> selectByVillageId(@Param("villageId") Integer villageId, 
                                             @Param("userId") Long userId,
                                             @Param("status") Integer status);

    /**
     * 统计民宿总数
     */
    long count(VillageHomestayPageQueryDTO dto);

    /**
     * 获取全局最大展示顺序（越小越靠前）
     */
    Integer getMaxDisplayNo();

    /**
     * 获取用于调整展示顺序的民宿列表（全局，按 display_no 升序返回）
     */
    List<VillageHomestay> listSortOptions();

    /**
     * 批量更新 display_no（全局；要求 sortRequests 中的顺序即展示顺序）
     */
    int batchUpdateDisplayNo(@Param("list") List<SortRequest> sortRequests);

    /**
     * 获取民宿总数（用于校验排序请求是否为全量）
     */
    int countAll();

    /**
     * 根据ID查询民宿（用于权限校验）
     */
    VillageHomestay selectByIdForCheck(@Param("id") Integer id);
}
