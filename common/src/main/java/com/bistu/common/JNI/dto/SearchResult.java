package com.bistu.common.JNI.dto;

import lombok.Data;

@Data
public class SearchResult {
    private int nPageNum;
    private CommonRect[] vecRect;

    public SearchResult() {
    }

    public SearchResult(int nPageNum, CommonRect[] pRects, int nRectCount) {
        this.nPageNum = nPageNum;
        vecRect = new CommonRect[nRectCount];
	    System.arraycopy(pRects, 0, vecRect, 0, nRectCount);
    }
}
