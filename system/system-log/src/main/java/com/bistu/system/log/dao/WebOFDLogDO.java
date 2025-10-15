package com.bistu.system.log.dao;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lenovo
 */
@Data
public class WebOFDLogDO {
    /**
     * @Param 文件ID
     */
    private String fileId;
    /**
     * @Param 文件后缀
     */
    private String suffix;
    /**
     * @Param 文件本地路径
     */
    private String path;
    /**
     * @Param 渲染图片精度
     */
    private Double dpr;
    /**
     * @Param 创建时间
     */
    private Date createTime;
    /**
     * @Param 创建人
     */
    private UserDO creator;
    /**
     * @Param 更改人
     */
    private UserDO updater;
    /**
     * @Param 列表项
     */
    private List<String> items;
    /**
     * @Param 扩展信息
     */
    private String[] extInfo;
    /**
     * @Param 错误码枚举
     */
    private String errorMsg;
    /**
     * @Param 执行结果
     */
    private Integer fail;
    @Data
    public static class UserDO {
        /**
         * @Param 用户ID
         */
        private Long userId;
        /**
         * @Param 用户姓名
         */
        private String userName;
    }
}
