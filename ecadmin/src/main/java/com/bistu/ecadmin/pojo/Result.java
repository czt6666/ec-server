package com.bistu.ecadmin.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 200;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 200;
        return result;
    }

    public static <T> Result<T> success(T object, String msg) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.msg = msg;
        result.code = 200;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

    /**
     * 自定义返回码和消息（可用于业务状态码）
     */
    public static <T> Result<T> of(int code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }

    /**
     * 自定义返回码和数据
     */
    public static <T> Result<T> of(int code, T data) {
        Result<T> result = new Result<>();
        result.code = code;
        result.data = data;
        return result;
    }

}
