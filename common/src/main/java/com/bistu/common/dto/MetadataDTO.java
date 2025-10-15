package com.bistu.common.dto;


import com.bistu.common.JNI.dto.MetaDateAdd;
import lombok.Data;

@Data
public class MetadataDTO {
    private MetaDateAdd[] data;
    private MetaDateAdd[] dataDisp;
    private MetaDateAdd[] dataGw;
    private long xServer;

    public MetadataDTO() {
    }

    public MetadataDTO(MetaDateAdd[] data, MetaDateAdd[] dataDisp, MetaDateAdd[] dataGw, long xServer) {
        this.data = data;
        this.dataDisp = dataDisp;
        this.dataGw = dataGw;
        this.xServer = xServer;
    }

}
