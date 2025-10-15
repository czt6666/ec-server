package com.bistu.common.JNI.dto;

import java.util.Arrays;

/**
 * @author: LL
 * @date: 2023/7/12
 * @description: 打印数据类
 */
public class stcPrint {
    public Integer[] pbyImage;//图像二进制数据
    public long nImgWidth;//像素大小
    public long nImgHeight;//像素大小

    public long back;


    public stcPrint(int size) {
        this.pbyImage = new Integer[size];
        this.nImgWidth = 0;
        this.nImgHeight = 0;
        this.back=0;
    }


    @Override
    public String toString() {
        return "stcPrint{" +
                "pbyImage=" + Arrays.toString(pbyImage) +
                ", nImgWidth=" + nImgWidth +
                ", nImgHeight=" + nImgHeight +
                ", back=" + back +
                '}';
    }
}
