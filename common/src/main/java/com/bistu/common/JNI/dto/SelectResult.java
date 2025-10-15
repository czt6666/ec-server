package com.bistu.common.JNI.dto;

import lombok.Data;

import java.util.Arrays;

@Data
public class SelectResult {
    public String str;
    public CommonRect [] pBoxList ;
    public int nPageNum;

    public SelectResult(String str, CommonRect[] pBoxList, int nPageNum) {
        this.str = str;
        this.pBoxList = pBoxList;
        this.nPageNum = nPageNum;
    }

    @Override
    public String toString() {
        return "SelectResult{" +
                "str='" + str + '\'' +
                ", pBoxList=" + Arrays.toString(pBoxList) +
                ", nPageNum=" + nPageNum +
                '}';
    }
}
