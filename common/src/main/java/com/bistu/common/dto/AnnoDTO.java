package com.bistu.common.dto;

import com.bistu.common.JNI.dto.AnnoHide;
import com.bistu.common.JNI.dto.CommonRect;
import com.bistu.common.JNI.dto.DK_POS;
import com.bistu.common.JNI.dto.StcAnnoInfo;
import lombok.Data;

/**
 * @author: zxh
 * @date: 2024/4/15 17:16
 * @description:
 */
@Data
public class AnnoDTO {
	private long mXServerSdk;
	private CommonRect rect;
	private int nPageNum;
	private String strPath;
	private StcAnnoInfo[] addPathStyle;
	private String[] replies;
	private long annoId;
	private DK_POS point;
	private AnnoHide[] annoHides;
	private int width;
	private int height;

	public AnnoDTO() {
	}

	public AnnoDTO(long mXServerSdk, CommonRect rect, int nPageNum, String strPath, StcAnnoInfo[] addPathStyle, String[] replies, long annoId, DK_POS point) {
		this.mXServerSdk = mXServerSdk;
		this.rect = rect;
		this.nPageNum = nPageNum;
		this.strPath = strPath;
		this.addPathStyle = addPathStyle;
		this.replies = replies;
		this.annoId = annoId;
		this.point = point;
	}
}
