package com.bistu.common.JNI.dto;

/**
 * @author: zxh
 * @date: 2023/6/13 0:01
 * @description: 某一个注释图元的属性等相关信息，该类表示一个页面注释的一条信息
 */
public class StcAnnoInfo {

	// 注释图元Id标识
	public long nameId;

	// 上述nameId对应的值
	public String strVal;

	public StcAnnoInfo() {
		nameId = -1;
		strVal = "";
	}

	public StcAnnoInfo(long nameId, String strVal) {
		this.nameId = nameId;
		this.strVal = strVal;
	}

	@Override
	public String toString() {
		return "nameId:" + nameId + "-strVal:" + strVal;
	}
}
