package com.bistu.ecadmin.service.impl;

import com.bistu.ecadmin.dao.TourCompanyMapper;
import com.bistu.ecadmin.pojo.PageResult;
import com.bistu.ecadmin.pojo.Result;
import com.bistu.ecadmin.pojo.TourCompany;
import com.bistu.ecadmin.service.TourCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourCompanyServiceImpl implements TourCompanyService {

    @Autowired
    private TourCompanyMapper tourCompanyMapper;

    @Override
    public Result<?> create(TourCompany company) {
        if (company.getName() == null || company.getName().trim().isEmpty()) {
            return Result.error("公司名称不能为空");
        }
        int dup = tourCompanyMapper.countByName(company.getName(), null);
        if (dup > 0) {
            return Result.error("公司名称已存在");
        }
        if (company.getBusinessStatus() == null) {
            company.setBusinessStatus(1);
        }
        if (company.getStatus() == null) {
            company.setStatus(1);
        }
        int inserted = tourCompanyMapper.insert(company);
        return inserted > 0 ? Result.success("创建成功") : Result.error("创建失败");
    }

    @Override
    public Result<?> update(TourCompany company) {
        TourCompany old = tourCompanyMapper.selectById(company.getId());
        if (old == null) {
            return Result.error("公司不存在");
        }
        if (company.getName() != null && !company.getName().equals(old.getName())) {
            int dup = tourCompanyMapper.countByName(company.getName(), company.getId());
            if (dup > 0) {
                return Result.error("公司名称已存在");
            }
        }
        int updated = tourCompanyMapper.update(company);
        return updated > 0 ? Result.success("更新成功") : Result.error("更新失败");
    }

    @Override
    public Result<?> delete(Long id) {
        TourCompany old = tourCompanyMapper.selectById(id);
        if (old == null) {
            return Result.error("公司不存在");
        }
        int deleted = tourCompanyMapper.delete(id);
        return deleted > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    @Override
    public Result<PageResult<TourCompany>> list(Integer page, Integer limit, String name, Integer status) {
        int p = (page == null || page < 1) ? 1 : page;
        int l = (limit == null || limit < 1) ? 10 : limit;
        int offset = (p - 1) * l;
        List<TourCompany> list = tourCompanyMapper.page(name, status, offset, l);
        int total = tourCompanyMapper.count(name, status);
        PageResult<TourCompany> pr = new PageResult<>();
        pr.setTotal(total);
        pr.setRecords(list);
        return Result.success(pr);
    }

    @Override
    public TourCompany getById(Long id) {
        return tourCompanyMapper.selectById(id);
    }
}

