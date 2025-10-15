package com.bistu.common.JNI.dto;

import lombok.Data;

import java.util.Arrays;

/**
 * @author: ll
 * @date: 2023/9/14
 * @description:
 */
@Data
public class SelectResult2 {
    public String[] str;//文字内容
    public CommonRect [] pBoxList ; //文字所在位置
    public int nPageNum;//页码

    public SelectResult2() {
    }

    public SelectResult2(String[] str, CommonRect[] pBoxList, int nPageNum) {
        this.str = str;
        this.pBoxList = pBoxList;
        this.nPageNum = nPageNum;
    }

    @Override
    public String toString() {
        return "selectResult{" +
                "str='" + Arrays.toString(str) + '\'' +
                ", pBoxList=" + Arrays.toString(pBoxList) +
                ", nPageNum=" + nPageNum +
                '}';
    }
}
