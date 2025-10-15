package com.bistu.common.dto;

import lombok.Data;

/**
 * @author: zxh
 * @date: 2024/5/6 14:42
 * @description:
 */
@Data
public class PdfToOfdDTO {
	private String pdfPath;
	private String ofdPath;

	public PdfToOfdDTO() {
	}
}
