package com.bistu.common.JNI.dto;

import lombok.Data;

/**
 * @author: LL
 * @date: 2023/7/12
 * @description: 图像水印类
 */
@Data
public class stcImgWatermark {
	private String strWatermarksName;
	private boolean bForeground;
	private String strImgFileUrl;
	private int dpi4Img;
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

	public stcImgWatermark() {
		strWatermarksName = " ";
		bForeground = false;
		strImgFileUrl = " ";
		dpi4Img = 96;

		dAlpha = 1.0;
		dRotate = 0;
		dHori = 0;
		dVert = 0;
		nHType = 0;
		nVType = 0;
		nPageStart = 1;
		nPageEnd = -1;
		nRegionType = 0;
		bTile = false;
		dTileXDistance = 0;
		dTileYDistance = 0;
		nTileTemplateType = 0;
	}

	;

	@Override
	public String toString() {
		return "stcImgWatermark{" +
				"strWatermarksName='" + strWatermarksName + '\'' +
				", bForeground=" + bForeground +
				", strImgFileUrl='" + strImgFileUrl + '\'' +
				", dpi4Img=" + dpi4Img +
				", dAlpha=" + dAlpha +
				", dRotate=" + dRotate +
				", dHori=" + dHori +
				", dVert=" + dVert +
				", nHType=" + nHType +
				", nVType=" + nVType +
				", nPageStart=" + nPageStart +
				", nPageEnd=" + nPageEnd +
				", nRegionType=" + nRegionType +
				", bTile=" + bTile +
				", dTileXDistance=" + dTileXDistance +
				", dTileYDistance=" + dTileYDistance +
				", nTileTemplateType=" + nTileTemplateType +
				'}';
	}
}
