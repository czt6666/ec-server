package com.bistu.ecadmin.pojo;

/**
 * 收藏相关的业务状态码定义
 */
public enum FavoriteStatusCode {
    SUCCESS(200),
    ALREADY_COLLECTED(400), // 已收藏（业务层面）
    NOT_LOGGED_IN(401),    // 未登录
    TOKEN_EXPIRED(403),    // token 过期（未使用）
    OTHER_ERROR(500);      // 其他错误

    private final int code;

    FavoriteStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}


