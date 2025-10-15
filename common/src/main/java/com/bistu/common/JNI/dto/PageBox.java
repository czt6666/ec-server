package com.bistu.common.JNI.dto;

public class PageBox {
    public String pageSize;
    public String pageNum;

    public PageBox(String pageSize, String pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    @Override
    public String toString() {
        return "pageBOX{" +
                "pageSize='" + pageSize + '\'' +
                ", pageNum='" + pageNum + '\'' +
                '}';
    }
}
