package com.bistu.common.JNI.dto;

import lombok.Data;

@Data
public class stcAttach {
    private long id;//name's ID, 作为输入参数时该id无效
    private String strName;//附件名, 新增附件/换名时内部都会copy一份(strName为空时无效)：即外层传入的strName若是new出来的buffer,需要外层析构。
    private String strTmpFileUrl;//附件从文档包内提取到本地的临时文件
    private String strOriFileUrl;//附件真实地址，用于xbrl解析
    private String strFormat;//附件格式名称，通常等价附件文件的扩展名称
    private String strCreationDate;//附件创建日期
    private String strModDate;//附件修改日期
    private double nSize;//附件大小

    public boolean	bVisible;//附件是否可见
    public String strUsage;//附件用途

    //默认构造参数
    public stcAttach() {
        this.id = 0;
        this.strName = "";
        this.strTmpFileUrl = "";
        this.strOriFileUrl = "";
        this.strFormat = "";
        this.strCreationDate = "";
        this.strModDate = "";
        this.nSize = 0;
        this.bVisible = true;
        this.strUsage = "";
    }
    //恢复到默认构造参数--用于循环读取文档中附件信息前的初始化
    public void ctor(){
        this.id = 0;
        this.strName = "";
        this.strTmpFileUrl = "";
        this.strOriFileUrl = "";
        this.strFormat = "";
        this.strCreationDate = "";
        this.strModDate = "";
        this.nSize = 0;
        this.bVisible = true;
        this.strUsage = "";
    }

    @Override
    public String toString() {
        return "stcAttach{" +
                "id=" + id +
                ", strName='" + strName + '\'' +
                ", strTmpFileUrl='" + strTmpFileUrl + '\'' +
                ", strOriFileUrl='" + strOriFileUrl + '\'' +
                ", strFormat='" + strFormat + '\'' +
                ", strCreationDate='" + strCreationDate + '\'' +
                ", strModDate='" + strModDate + '\'' +
                ", nSize=" + nSize +
                ", bVisible=" + bVisible +
                ", strUsage='" + strUsage + '\'' +
                '}';
    }
}
