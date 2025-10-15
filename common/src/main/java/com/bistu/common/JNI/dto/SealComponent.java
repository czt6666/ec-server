package com.bistu.common.JNI.dto;


import lombok.Data;

/**
 * @author: LL
 * @date: 2024/1/18
 * @description: 签章组件下的签章类
 */
@Data
public class SealComponent {

    private String sealName;//签章名称
    private int imgWidth;//签章宽
    private int imgHeight;//签章高

    public SealComponent() {
    }

    public SealComponent(String sealName, int imgWidth, int imgHeight) {
        this.sealName = sealName;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
    }

    @Override
    public String toString() {
        return "SealComponent{" +
                "sealName='" + sealName + '\'' +
                ", imgWidth=" + imgWidth +
                ", imgHeight=" + imgHeight +
                '}';
    }
}
