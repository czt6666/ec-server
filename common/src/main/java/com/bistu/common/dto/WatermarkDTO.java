package com.bistu.common.dto;

import lombok.Data;

@Data
public class WatermarkDTO {
	private long mXServerSdk;

	// 图片水印特有属性
	private String strImgFileUrl;
	private int dpi4Img;

	// 文字水印特有属性
	private String strText;// 水印内容
	private String strFontName;// 水印字体
	private double dFontSize;// 水印字体大小
	private int r, g, b;// 文本颜色
	private int width, height;// 添加水印文本串box:高和宽(不旋转时的)

	// 共有属性
	private String strWatermarksName;
	private boolean bForeground;
	private double dAlpha;
	private double dRotate;
	private double dHori;
	private double dVert;
	private int nHType;
	private int nVType;
	private int nPageStart;
	private int nPageEnd;
	private int nRegionType;// 2023.2.20 sc add 新增参数 区域性水印类型。0-整体可添加水印，1-上半部分可添加水印，2-下半部分可添加水印，3-.....暂定3个值，后续可扩充
	// 2023.3.7 sc add 新增3个变量控制平铺
	private boolean bTile;// 是否进行平铺
	private double dTileXDistance;// 单位mm，平铺横向间距//根据此参数可以得知横向需要铺多少个水印
	private double dTileYDistance;// 单位mm，平铺纵向间距//根据此参数可以得知纵向需要铺多少个水印
	// 2023.4.24 sc add 新增平铺模板模式类型
	// bTile优先，如果bTile为false，nTileTemplateType不管是多少都不会是平铺水印
	private int nTileTemplateType;

	private int watermarkType;// 0-文字 1-图片 自定义属性，用于判断水印类型

	public WatermarkDTO() {
		strText = "";
		strFontName = "宋体";
		dFontSize = 24;
		r = 255;
		g = 0;
		b = 0;
		width = 0;
		height = 0;
		strImgFileUrl = "";
		dpi4Img = 96;
		strWatermarksName = "";
		bForeground = false;
		dAlpha = 1.0;
		dRotate = 0;
		dHori = 0;
		dVert = 0;
		nHType = 1;
		nVType = 1;
		nPageStart = 1;
		nPageEnd = -1;
		nRegionType = 0;
		bTile = false;
		dTileXDistance = 0;
		dTileYDistance = 0;
		nTileTemplateType = 0;
	}

	public WatermarkDTO(long mXServerSdk) {
		this.mXServerSdk = mXServerSdk;
	}

	public WatermarkDTO(String strImgFileUrl, int dpi4Img, long mXServerSdk) {
		this.strImgFileUrl = strImgFileUrl;
		this.dpi4Img = dpi4Img;
		this.mXServerSdk = mXServerSdk;
	}

	public WatermarkDTO(long mXServerSdk, String strText, String strFontName, double dFontSize, int r, int g, int b, int width, int height) {
		this.mXServerSdk = mXServerSdk;
		this.strText = strText;
		this.strFontName = strFontName;
		this.dFontSize = dFontSize;
		this.r = r;
		this.g = g;
		this.b = b;
		this.width = width;
		this.height = height;
	}
}
