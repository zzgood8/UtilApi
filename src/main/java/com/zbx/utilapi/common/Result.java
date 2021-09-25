package com.zbx.utilapi.common;

import lombok.Getter;

/**
 * @创建人 zbx
 * @创建时间 2021/9/10
 * @描述
 */
@Getter
public class Result<T> {

    private String code;
    private String msg;
    private T data;

    private Result() {}

    private Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T>  success() {
        return new Result<T>("200", "操作成功", null);
    }

    public static <T> Result<T>  success(T data) {
        return new Result<T>("200", "操作成功", data);
    }

    public static <T> Result<T>  failed() {
        return new Result<T>("500", "操作失败", null);
    }

    public static <T> Result<T>  failed(T data) {
        return new Result<T>("500", "操作失败", data);
    }

}
