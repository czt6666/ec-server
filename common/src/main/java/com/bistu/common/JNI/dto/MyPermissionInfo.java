package com.bistu.common.JNI.dto;

import lombok.Data;

/**
 * @author: LL
 * @date: 2023/7/12
 * @description: 权限信息
 */
@Data
public class MyPermissionInfo {
	private boolean isEdit, isAnnot, isExport, isSignature, isWatermark, isPrintScreen, isPrint;// 是否允许编辑：文档[all]|注释|导出(保存)|签名|水印，以及是否允许屏幕打印、打印
	private int nPrintCopies;// 2020.1.3 add打印份数限制， 负数表示打印份数不限制;0禁止打印(尽管bPrint为true);正数表示最大可打印份数
	private String strDate4Start, strDate4End;

    public MyPermissionInfo(boolean isEdit, boolean isAnnot, boolean isExport, boolean isSignature, boolean isWatermark, boolean isPrintScreen, boolean isPrint, int nPrintCopies, String strDate4Start, String strDate4End) {
        this.isEdit = isEdit;
        this.isAnnot = isAnnot;
        this.isExport = isExport;
        this.isSignature = isSignature;
        this.isWatermark = isWatermark;
        this.isPrintScreen = isPrintScreen;
        this.isPrint = isPrint;
        this.nPrintCopies = nPrintCopies;
        this.strDate4Start = strDate4Start;
        this.strDate4End = strDate4End;
    }

    public MyPermissionInfo() {
        isEdit = true;
		isAnnot = true;
		isExport = true;
		isSignature = true;
		isWatermark = true;
		isPrintScreen = true;
		isPrint = true;
		nPrintCopies = -1;
		strDate4Start = "";
		strDate4End = " ";
	}

    @Override
	public String toString() {
		return "MyPermissionInfo{" +
				"isEdit=" + isEdit +
				", isAnnot=" + isAnnot +
				", isExport=" + isExport +
				", isSignature=" + isSignature +
				", isWatermark=" + isWatermark +
				", isPrintScreen=" + isPrintScreen +
				", isPrint=" + isPrint +
				", nPrintCopies=" + nPrintCopies +
				", strDate4Start='" + strDate4Start + '\'' +
				", strDate4End='" + strDate4End + '\'' +
				'}';
	}
}
