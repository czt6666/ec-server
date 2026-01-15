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

    @ApiModelProperty(value = "关联用户ID（商家ID）", required = true)
    private Long userId;

    @ApiModelProperty("所属乡村ID")
    private Integer villageId;
    
    @ApiModelProperty("民宿名称")
    private String homestayName;
    
    @ApiModelProperty("民宿详细地址")
    private String address;
    
    @ApiModelProperty("营业状态：1-营业中（上架），2-待审核/下架（统一视为待审核）")
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
    
    @ApiModelProperty("封面图")
    private String coverImage;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("纬度")
    private Double latitude;        // 纬度
    @ApiModelProperty("经度")
    private Double longitude;// 经度
    @ApiModelProperty("资质凭证")
    private String qualificationImages;
    @ApiModelProperty("链接地址")
    private String linkAddress;     // 链接地址
    @ApiModelProperty("收藏数量")
    private Long collectNumber;
    
    @ApiModelProperty("当前用户是否已收藏：0-未收藏，1-已收藏")
    private Integer isCollect;
    
    @ApiModelProperty("商户用户名")
    private String userName;
}