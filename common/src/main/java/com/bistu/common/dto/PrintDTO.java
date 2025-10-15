package com.bistu.common.dto;

import lombok.Data;

@Data
public class PrintDTO {

    private long xServer;
    private String strPrintParamXML;
    private int[] pageRange;
    private String strTmpResImgUrl;
    private double scale;
    private int dpi;

    public PrintDTO() {
    }

    public PrintDTO(long xServer, String strPrintParamXML, int[] pageRange, String strTmpResImgUrl, double scale, int dpi) {
        this.xServer = xServer;
        this.strPrintParamXML = strPrintParamXML;
        this.pageRange = pageRange;
        this.strTmpResImgUrl = strTmpResImgUrl;
        this.scale = scale;
        this.dpi = dpi;
    }

    public PrintDTO(long xServer, String strPrintParamXML, int[] pageRange, String strTmpResImgUrl, double scale) {
        this.xServer = xServer;
        this.strPrintParamXML = strPrintParamXML;
        this.pageRange = pageRange;
        this.strTmpResImgUrl = strTmpResImgUrl;
        this.scale = scale;
    }

    public PrintDTO(long xServer, int[] pageRange, String strTmpResImgUrl, double scale, int dpi) {
        this.xServer = xServer;
        this.pageRange = pageRange;
        this.strTmpResImgUrl = strTmpResImgUrl;
        this.scale = scale;
        this.dpi = dpi;
    }
}
