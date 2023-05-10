package com.xunye.core.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class R<T> {

    private int code;
    private String msg;
    private T data;
    private long total;

    public R(int code, String msg, T data, long total) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.total = total;
    }

    public static <T> R<T> success(int code, String msg, T data, long total) {
        return new R(code, msg, data, total);
    }

    public static <T> R<T> success(int code, String msg) {
        return new R(code, msg, null, 0);
    }

    public static <T> R<T> success(String msg, T data, long total) {
        return success(RStateEnum.SUCCESS.getCode(), msg, data, total);
    }

    public static <T> R<T> success(T data, long total) {
        return success(RStateEnum.SUCCESS.getMsg(), data, total);
    }

    public static <T> R<T> success(int code, String msg, T data) {
        return success(code, msg, data, 0);
    }

    public static <T> R<T> success(String msg, T data) {
        return success(msg, data, 0);
    }

    public static <T> R<T> success(T data) {
        return success(data, 0);
    }

    public static <T> R<T> success(String msg) {
        return success(msg, null);
    }

    public static <T> R<T> success() {
        return success(null);
    }

    // ==============================================================================  //

    public static <T> R<T> failure(int code, String msg, T data, long total) {
        return new R(code, msg, data, total);
    }

    public static <T> R<T> failure(String msg, T data, long total) {
        return failure(RStateEnum.BUSINESS_ERROR.getCode(), msg, data, total);
    }

    public static <T> R<T> failure(T data, long total) {
        return failure(RStateEnum.BUSINESS_ERROR.getMsg(), data, total);
    }

    public static <T> R<T> failure(int code, String msg, T data) {
        return failure(code, msg, data, 0);
    }

    public static <T> R<T> failure(int code, String msg) {
        return failure(code, msg, null);
    }

    public static <T> R<T> failure(String msg, T data) {
        return failure(msg, data, 0);
    }

    public static <T> R<T> failure(T data) {
        return failure(data, 0);
    }

    public static <T> R<T> failure(String msg) {
        return failure(msg, null);
    }

    public static <T> R<T> failure() {
        return failure(null);
    }

    public boolean checkSuccess() {
        return code == RStateEnum.SUCCESS.getCode();
    }

    public boolean checkError() {
        return !checkSuccess();
    }

}
