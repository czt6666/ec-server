package com.bistu.common.JNI.dto;

import lombok.Data;

import java.util.Arrays;

/**
 * @author: zxh
 * @date: 2023/9/14 9:10
 * @description:
 */
@Data
public class AnnoResult {
	private int nPageNum;//注释所在页码
	private StcAnnoInfo[] annoInfo;//注释信息
	private CommonRect box;//注释外接box
	private String[] replies;//注释回复信息
	private String path;//形状注释点位信息
	private String[] text;/*2024.3.4 add ll*/
	private String[] annoProperty;/* 2024-07-01 zxh-add ：保存注释的属性信息，例如：线宽、颜色、透明度等*/

	public AnnoResult() {
	}

	public AnnoResult(int nPageNum, StcAnnoInfo[] annoInfo, CommonRect box, String[] replies, String path, String[] text, String[] annoProperty) {
		this.nPageNum = nPageNum;
		this.annoInfo = annoInfo;
		this.box = box;
		this.replies = replies;
		this.path = path;
		this.text = text;
		this.annoProperty = annoProperty;
	}

	@Override
	public String toString() {
		return "AnnoResult{" +
				"nPageNum=" + nPageNum +
				", annoInfo=" + Arrays.toString(annoInfo) +
				", box=" + box +
				", replies=" + Arrays.toString(replies) +
				", path='" + path + '\'' +
				", text=" + Arrays.toString(text) +
				", annoProperty='" + Arrays.toString(annoProperty) +
				'}';
	}
}
