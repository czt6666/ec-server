package com.bistu.ecadmin.dao;

import com.bistu.ecadmin.pojo.TourCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TourCompanyMapper {

    int insert(TourCompany company);

    int update(TourCompany company);

    int delete(@Param("id") Long id);

    TourCompany selectById(@Param("id") Long id);

    int countByName(@Param("name") String name, @Param("excludeId") Long excludeId);

    int countByUnifiedSocialCreditCode(@Param("unifiedSocialCreditCode") String unifiedSocialCreditCode, @Param("excludeId") Long excludeId);

    List<TourCompany> page(@Param("name") String name,
                           @Param("status") Integer status,
                           @Param("merchantUserId") Long merchantUserId,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    int count(@Param("name") String name, @Param("status") Integer status, @Param("merchantUserId") Long merchantUserId);
}

