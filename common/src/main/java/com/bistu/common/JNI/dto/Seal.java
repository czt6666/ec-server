package com.bistu.common.JNI.dto;

/**
 * @author: LL
 * @date: 2024/1/18
 * @description: 文档的签章类
 */

public class Seal {

    public int nPageNum;//签章所在页码
    public CommonRect box;//签章所在具体位置
    public int nImgWidth;//签章本身的宽
    public int nImgHeight;//签章本身的高

    public String strVal;//签章属性

    public Seal() {
    }

    public Seal(int nPageNum, CommonRect box, int nImgWidth, int nImgHeight, String strVal) {
        this.nPageNum = nPageNum;
        this.box = box;
        this.nImgWidth = nImgWidth;
        this.nImgHeight = nImgHeight;
        this.strVal = strVal;
    }

    @Override
    public String toString() {
        return "Seal{" +
                "nPageNum=" + nPageNum +
                ", box=" + box +
                ", nImgWidth=" + nImgWidth +
                ", nImgHeight=" + nImgHeight +
                ", strVal='" + strVal + '\'' +
                '}';
    }
}
