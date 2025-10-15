package com.bistu.common.dto;

import lombok.Data;

@Data
public class ServerDTO {
    private long mXServerSdk;
    private long mXViewer;
    private long pageCount;
    private int DocumentCount;

    public ServerDTO() {
    }

    public ServerDTO(long mXServerSdk, long mXViewer, long pageCount, int DocumentCount){
        this.mXServerSdk =  mXServerSdk;
        this.mXViewer =  mXViewer;
        this.pageCount =  pageCount;
        this.DocumentCount = DocumentCount;
    }
}
