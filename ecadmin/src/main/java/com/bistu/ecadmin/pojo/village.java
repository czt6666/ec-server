package com.bistu.ecadmin.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class village {

        @ApiModelProperty(value = "主键ID")
        private Integer id;

        @ApiModelProperty(value = "乡村名称", required = true)
        private String villageName;

        @ApiModelProperty(value = "详细地址")
        private String address;

        @ApiModelProperty(value = "乡村描述")
        private String villageDescription;

        @ApiModelProperty(value = "乡村管理人数", example = "150")
        private Integer managerCount = 150;

        @ApiModelProperty(value = "户数")
        private Integer householdCount;

        @ApiModelProperty(value = "村书记姓名")
        private String secretaryName;

        @ApiModelProperty(value = "村书记联系方式")
        private String secretaryPhone;

        @ApiModelProperty(value = "总面积（亩）")
        private BigDecimal totalArea;

        @ApiModelProperty(value = "耕地面积（亩）")
        private BigDecimal farmlandArea;

        @ApiModelProperty(value = "林地面积（亩）")
        private BigDecimal forestArea;

        @ApiModelProperty(value = "水域面积（亩）")
        private BigDecimal waterArea;

        @ApiModelProperty(value = "建设用地面积（亩）")
        private BigDecimal constructionArea;

        @ApiModelProperty(value = "创建时间")
        private LocalDateTime createTime;

        @ApiModelProperty(value = "修改时间")
        private LocalDateTime updateTime;
}
