package com.bistu.common.JNI.dto;

import lombok.Data;

import java.util.Arrays;
/**
 * @author: LL
 * @date: 2024/1/18
 * @description: 签章组件类
 */
@Data
public class SealComponentList {
    private SealComponent[] sealComList;//签章组件内的所有签章信息
    private String strVenderId;//签章组件名称

    public SealComponentList() {
    }

    public SealComponentList(SealComponent[] sealComList, String strVenderId) {
        this.sealComList = sealComList;
        this.strVenderId = strVenderId;
    }

    @Override
    public String toString() {
        return "SealComponentList{" +
                "sealComList=" + Arrays.toString(sealComList) +
                ", strVenderId='" + strVenderId + '\'' +
                '}';
    }
}
