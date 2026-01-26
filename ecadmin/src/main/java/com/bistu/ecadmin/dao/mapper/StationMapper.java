package com.bistu.ecadmin.dao.mapper;

import com.bistu.ecadmin.dao.DTO.StationPageQueryDTO;
import com.bistu.ecadmin.pojo.Station;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StationMapper {

    /**
     * 分页查询驿站列表（支持模糊查询）
     */
    Page<Station> pageQuery(StationPageQueryDTO dto);

    /**
     * 根据ID查询驿站详情
     */
    Station selectById(@Param("id") Long id,
                       @Param("userId") Long userId,
                       @Param("status") Integer status,
                       @Param("merchantUserId") Long merchantUserId);

    /**
     * 新增驿站
     */
    int insert(Station station);

    /**
     * 更新驿站
     */
    int update(Station station);

    /**
     * 删除驿站
     */
    int deleteById(@Param("id") Long id);

    /**
     * 统计驿站总数
     */
    long count(StationPageQueryDTO dto);

    /**
     * 按名称统计数量（用于重名校验）
     * @param name 名称
     * @param excludeId 排除的ID（更新时传自身ID）
     */
    long countByName(@Param("name") String name, @Param("excludeId") Long excludeId);

    /**
     * 按统一社会信用代码统计数量（用于唯一性校验）
     * @param unifiedSocialCreditCode 统一社会信用代码
     * @param excludeId 排除的ID（更新时传自身ID）
     */
    long countByUnifiedSocialCreditCode(@Param("unifiedSocialCreditCode") String unifiedSocialCreditCode, @Param("excludeId") Long excludeId);

    /**
     * 查询所有驿站（用于导出）
     */
    List<Station> listAll();

    /**
     * 按商户用户ID查询驿站（用于商户导出）
     */
    List<Station> listByMerchantUserId(@Param("merchantUserId") Long merchantUserId);
}

