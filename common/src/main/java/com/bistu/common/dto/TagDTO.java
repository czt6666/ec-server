package com.bistu.common.dto;

import com.bistu.common.JNI.dto.CommonRect;
import lombok.Data;

@Data
public class TagDTO {
    private long mXServerSdk;
    private String  importFilePath;
    private String exportFilePath;
    private int triggerPos;
    private long id;
    private int nPageNum;
    private long fatherId;
    private int addMod;
    private String addName;
    private long selfPos;
    private CommonRect selectBox;

    public TagDTO(long mXServerSdk) {
        this.mXServerSdk = mXServerSdk;
    }
    public TagDTO(){}


}
