package com.bistu.common.dto;

import lombok.Data;

@Data
public class DocPermissionDTO {
    private long mXServerSdk;
    private boolean edit;
    private boolean anno;
    private boolean export;
    private boolean sign;
    private boolean waterMark;
    private boolean screenShot;
    private boolean print;
    public DocPermissionDTO(long mXServerSdk) {
        this.mXServerSdk = mXServerSdk;
    }
    public DocPermissionDTO(){}
}
