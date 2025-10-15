package com.bistu.common.JNI.dto;

import java.util.Arrays;

public class stcSigns {
    public Integer[] pbyImage;//图像二进制数据
    public int nImgWidth;//像素大小
    public int nImgHeight;//像素大小

    public int back;//printpage返回值

    public stcSigns(int size) {
        this.pbyImage = new Integer[size];
        this.nImgWidth = 0;
        this.nImgHeight = 0;
        this.back = 0;
    }

    @Override
    public String toString() {
        return "stcSigns{" +
                "pbyImage=" + Arrays.toString(pbyImage) +
                ", nImgWidth=" + nImgWidth +
                ", nImgHeight=" + nImgHeight +
                ", back=" + back +
                '}';
    }
}
