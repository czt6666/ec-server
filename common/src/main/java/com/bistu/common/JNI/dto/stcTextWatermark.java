package com.bistu.common.JNI.dto;

import lombok.Data;

/**
 * @author: LL
 * @date: 2023/7/12
 * @description: 文本水印类
 */
@Data
public class stcTextWatermark {
	private String strWatermarksName;// 水印名称,可以为空. 读取时若为空，UI管理控件可以自定义给出水印名称
	private boolean bForeground;// 是否为前景水印
	private String strText;// 水印内容
	private String strFontName;// 水印字体

	private double dFontSize;// 水印字体大小
	private int r, g, b;// 文本颜色
	private double dAlpha;// 不透明度从0到1.0取值
	private double dRotate;// 逆时针角度
	private double dHori;// 水平对齐
	private double dVert;// 垂直对齐
	private int nHType;// 水平对齐：0-左对齐;1-居中;2-右对齐
	private int nVType;// 垂直对齐：0-顶端;1-居中
	private int nPageStart;// 水印应用的页码范围--开始值，最小值1
	private int nPageEnd;// 水印应用的页码范围--结束值，最大值不超过当前文档的总页数，默认-1值代表当前文档总页数.
	private int width, height;// 添加水印文本串box:高和宽(不旋转时的)
	private int nRegionType;// 区域性水印类型。0-整体可添加水印，1-上半部分可添加水印，2-下半部分可添加水印，3-.....暂定3个值
	private boolean bTile;// 是否进行平铺
	private double dTileXDistance;// 单位mm，平铺横向间距//根据此参数可以得知横向需要铺多少个水印
	private double dTileYDistance;// 单位mm，平铺纵向间距//根据此参数可以得知纵向需要铺多少个水印
	private int nTileTemplateType;// 平铺模板模式类型：0-不使用平铺模板;1-一页二行;2-一页四行;3-一页六行;4-一页八行;5-一页十二行....后续待扩展

	public stcTextWatermark() {
		strWatermarksName = "";
		bForeground = false;
		strText = "";
		strFontName = "宋体";
		dFontSize = 24;
		r = 255;
		g = 0;
		b = 0;
		dAlpha = 1.0;
		dRotate = 0;
		dHori = 0;
		dVert = 0;
		nHType = 1;
		nVType = 1;
		nPageStart = 1;
		nPageEnd = -1;
		width = 0;
		height = 0;
		nRegionType = 0;
		bTile = false;
		dTileXDistance = 0;
		dTileYDistance = 0;
		nTileTemplateType = 0;
	}


	@Override
	public String toString() {
		return "stcTextWatermark{" +
				"strWatermarksName='" + strWatermarksName + '\'' +
				", bForeground=" + bForeground +
				", strText='" + strText + '\'' +
				", strFontName='" + strFontName + '\'' +
				", dFontSize=" + dFontSize +
				", r=" + r +
				", g=" + g +
				", b=" + b +
				", dAlpha=" + dAlpha +
				", dRotate=" + dRotate +
				", dHori=" + dHori +
				", dVert=" + dVert +
				", nHType=" + nHType +
				", nVType=" + nVType +
				", nPageStart=" + nPageStart +
				", nPageEnd=" + nPageEnd +
				", width=" + width +
				", height=" + height +
				", nRegionType=" + nRegionType +
				", bTile=" + bTile +
				", dTileXDistance=" + dTileXDistance +
				", dTileYDistance=" + dTileYDistance +
				", nTileTemplateType=" + nTileTemplateType +
				'}';
	}
}
