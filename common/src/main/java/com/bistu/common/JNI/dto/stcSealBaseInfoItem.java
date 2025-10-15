package com.bistu.common.JNI.dto;

public class stcSealBaseInfoItem {
    public int nameId;
    public String strVal;

//    public stcSealBaseInfoItem() {
//        this.nameId = 0;
//        this.strVal = "";
//    }

    @Override
    public String toString() {
        return "stcSealBaseInfoItem{" +
                "nameId=" + nameId +
                ", strVal='" + strVal + '\'' +
                '}';
    }
}
