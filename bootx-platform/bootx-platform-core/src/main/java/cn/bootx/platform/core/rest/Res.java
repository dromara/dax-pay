package cn.bootx.platform.core.rest;

import cn.bootx.platform.core.rest.result.ErrorResult;
import cn.bootx.platform.core.rest.result.Result;

import static cn.bootx.platform.core.code.CommonCode.FAIL_CODE;
import static cn.bootx.platform.core.code.CommonCode.SUCCESS_CODE;

/**
 * 返回工具类
 *
 * @author xxm
 * @since 2020/1/22 15:29
 */
public class Res {

    private final static String SUCCESS = "success";

    private final static String FAILED = "failed";

    public static <T> Result<T> ok() {
        return new Result<>(SUCCESS_CODE, SUCCESS);
    }

    public static <T> Result<T> okAndMsg(String message) {
        return new Result<>(SUCCESS_CODE, message);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(SUCCESS_CODE, data, SUCCESS);
    }

    public static <T> Result<T> error() {
        return new Result<>(FAIL_CODE, FAILED);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(FAIL_CODE, message);
    }

    public static <T> Result<T> response(int code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> response(int code, String msg, String traceId) {
        return new ErrorResult<>(code, msg, traceId);
    }

    public static <T> Result<T> response(int code, String msg, T data) {
        return new Result<>(code, data, msg);
    }

}
