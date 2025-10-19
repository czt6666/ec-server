package com.bistu.ecadmin.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("乡村民宿信息")
public class VillageHomestay {
    
    @ApiModelProperty("主键ID")
    private Integer id;
    
    @ApiModelProperty("所属乡村ID")
    private Integer villageId;
    
    @ApiModelProperty("民宿名称")
    private String homestayName;
    
    @ApiModelProperty("民宿详细地址")
    private String address;
    
    @ApiModelProperty("营业状态：1-营业 2-暂停营业 3-已下架")
    private Integer status;
    
    @ApiModelProperty("星级/等级：0-未评 1-5星")
    private Integer starLevel;
    
    @ApiModelProperty("客房数量")
    private Integer roomCount;
    
    @ApiModelProperty("床位总数")
    private Integer bedCount;
    
    @ApiModelProperty("最大接待人数")
    private Integer maxCapacity;
    
    @ApiModelProperty("负责人姓名")
    private String contactName;
    
    @ApiModelProperty("负责人电话")
    private String contactPhone;
    
    @ApiModelProperty("民宿简介、特色亮点")
    private String description;
    
    @ApiModelProperty("房价:元/月")
    private BigDecimal price;
    
    @ApiModelProperty("封面图URL")
    private String coverImage;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}