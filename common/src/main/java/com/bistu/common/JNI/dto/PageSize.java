package com.bistu.common.JNI.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: zxh
 * @date: 2024/12/31 14:39
 * @description:
 */
@Data
public class PageSize implements Serializable {

	private int pageNum;
	private double width;
	private double height;
	private int rotate;

	public PageSize() {
	}

	public PageSize(int pageNum, double width, double height, int rotate) {
		this.pageNum = pageNum;
		this.width = width;
		this.height = height;
		this.rotate = rotate;
	}
}
