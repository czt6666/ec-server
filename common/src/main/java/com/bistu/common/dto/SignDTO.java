package com.bistu.common.dto;

import com.bistu.common.JNI.dto.CommonPos;
import lombok.Data;


@Data
public class SignDTO {
    private long mXServerSdk;
    private int signId;
    private CommonPos signPos;
    private int pageNum;

    public SignDTO() {
    }

    public SignDTO(long mXServerSdk, int signId, CommonPos signPos, int pageNum) {
        this.mXServerSdk = mXServerSdk;
        this.signId = signId;
        this.signPos = signPos;
        this.pageNum = pageNum;
    }
}