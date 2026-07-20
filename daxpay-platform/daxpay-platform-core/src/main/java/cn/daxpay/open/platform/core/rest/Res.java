package cn.daxpay.open.platform.core.rest;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.rest.result.Result;

/// # 返回工具类
///
public class Res {

    private final static String SUCCESS = "success";

    private final static String FAILED = "failed";

    public static <T> Result<T> ok() {
        return new Result<>(CommonCode.SUCCESS_CODE, SUCCESS);
    }

    public static <T> Result<T> okAndMsg(String message) {
        return new Result<>(CommonCode.SUCCESS_CODE, message);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(CommonCode.SUCCESS_CODE, data, SUCCESS);
    }

    public static <T> Result<T> error() {
        return new Result<>(CommonCode.FAIL_CODE, FAILED);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(CommonCode.FAIL_CODE, message);
    }

    public static <T> Result<T> response(int code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> response(int code, String msg, T data) {
        return new Result<>(code, data, msg);
    }

}
