package com.bistu.common.dto;

import lombok.Data;

@Data
public class Select2DTO {

    private long mXServerSdk;
    private int[] range;

    public Select2DTO() {
    }
}
