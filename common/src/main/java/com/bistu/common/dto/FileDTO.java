package com.bistu.common.dto;

import lombok.Data;

@Data
public class FileDTO {
    /**
     * strFileUrl 打开的文件所在目录
     * nDocTypeFlag 文件类型 ofd为1，pdf为3
     * strTmpResImgUrl 自定义输出的文件图像路径
     * mode 打印类型，1打印前两页，2打印所有页
     * scale 图像缩放比，若第一个参数中的缩放比有内容，会自动覆盖掉第一个参数有关缩放比的内容
     * strPrintParamXML 绘制需要的参数
     */
    private String strFileUrl;
    private int nDocTypeFlag;
    private String strTmpResImgUrl;
    private int mode;
    private double scale;
    private String strPrintParamXML;

    public FileDTO() {
    }

    public FileDTO(String strFileUrl, int nDocTypeFlag, String strTmpResImgUrl, int mode, double scale, String strPrintParamXML){
        this.strFileUrl = strFileUrl;
        this.nDocTypeFlag = nDocTypeFlag;
        this.strTmpResImgUrl =strTmpResImgUrl;
        this.mode = mode;
        this.scale = scale;
        this.strPrintParamXML = strPrintParamXML;
    }

    public FileDTO(String strFileUrl, int nDocTypeFlag){
        this.strFileUrl = strFileUrl;
        this.nDocTypeFlag = nDocTypeFlag;
    }
}
