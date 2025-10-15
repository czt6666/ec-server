package com.bistu.common.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MakeOFDDTO {
    private String templateJson;
    private String dataJson;
    private int outputMod;
    private String outputPath;

}
