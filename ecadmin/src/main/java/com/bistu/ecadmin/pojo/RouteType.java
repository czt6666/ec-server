package com.bistu.ecadmin.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 线路类型字典
 */
@Data
public class RouteType implements Serializable {

    private Long id;

    /**
     * 类型名称，例如：红色旅游、绿色旅游、组合
     */
    private String typeName;

    /**
     * 类型编码，可选
     */
    private String typeCode;

    /**
     * 排序，越小越靠前
     */
    private Integer sort;

    /**
     * 状态：1启用 0停用
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;
}


















