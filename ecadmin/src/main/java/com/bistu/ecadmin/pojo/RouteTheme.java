package com.bistu.ecadmin.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 线路主题字典
 */
@Data
public class RouteTheme implements Serializable {

    private Long id;

    /**
     * 主题名称，例如：西红东绿、红色教育、生态康养
     */
    private String themeName;

    /**
     * 主题编码，可选
     */
    private String themeCode;

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




