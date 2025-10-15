package com.bistu.common.dto;

import com.bistu.common.JNI.dto.CommonRect;
import lombok.Data;


@Data
public class TextDTO {
    private long mXServerSdk;

    // selectText函数使用页码及位置
    private int[] range;
    private CommonRect pos;

    // HighLightSelect函数使用的页码
    private int pageNum;

    // searchText使用的关键字、整词匹配等
    private String value;
    private boolean bCaseSensitive;
    private boolean bWholeWord;
    private boolean bMultiMatch;
    private int[] searchRange;


    public TextDTO() {
    }

    public TextDTO(long mXServerSdk, int[] range, CommonRect pos, int pageNum, String value, boolean bCaseSensitive, boolean bWholeWord, boolean bMultiMatch, int[] searchRange) {
        this.mXServerSdk = mXServerSdk;
        this.range = range;
        this.pos = pos;
        this.pageNum = pageNum;
        this.value = value;
        this.bCaseSensitive = bCaseSensitive;
        this.bWholeWord = bWholeWord;
        this.bMultiMatch = bMultiMatch;
        this.searchRange = searchRange;
    }
}