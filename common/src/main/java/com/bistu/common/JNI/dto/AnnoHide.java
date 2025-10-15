package com.bistu.common.JNI.dto;

import lombok.Data;

/**
 * @author: zxh
 * @date: 2024/8/7 11:27
 * @description: JNI传递隐藏注释信息
 */
@Data
public class AnnoHide {
	private long annoId;
	private int nPageNum;
	private boolean hide;

	public AnnoHide() {
	}

	public AnnoHide(long annoId, int nPageNum, boolean hide) {
		this.annoId = annoId;
		this.nPageNum = nPageNum;
		this.hide = hide;
	}
}
