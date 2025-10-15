package com.bistu.common.JNI.dto;

import java.util.Arrays;

public class stcSealProvider {
    public String strVenderId;
    public String[] vecSealName;

//    public stcSealProvider(String [] SealIn,int length) {
//
//       this.vecSealName = new String[length];
//       for(int i=0;i<length;i++)
//       {
//           this.vecSealName[i]=SealIn[i];
//       }
//    }

    public stcSealProvider(String strVenderId, String[] vecSealName) {
        this.strVenderId = strVenderId;
        this.vecSealName = vecSealName;
    }

    @Override
    public String toString() {
        return "stcSealProvider{" +
                "strVenderId='" + strVenderId + '\'' +
                ", vecSealName=" + Arrays.toString(vecSealName) +
                '}';
    }
}
