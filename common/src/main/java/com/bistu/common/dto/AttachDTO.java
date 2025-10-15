package com.bistu.common.dto;

import com.bistu.common.JNI.dto.stcAttach;
import lombok.Data;

@Data
public class AttachDTO {
    private long mXServerSdk;

    private String attachmentOnlineUrl;// 附件文件在线地址

    private String attachPath;//配置文件中的attachPath

    private stcAttach importAttachObj;

    public AttachDTO() {
    }

    public AttachDTO(long mXServerSdk) {
        this.mXServerSdk = mXServerSdk;
    }
}
