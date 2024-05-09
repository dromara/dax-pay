package cn.bootx.platform.common.core.rest;

import static cn.bootx.platform.common.core.code.CommonCode.FAIL_CODE;
import static cn.bootx.platform.common.core.code.CommonCode.SUCCESS_CODE;

/**
 * 返回工具类
 *
 * @author xxm
 * @since 2020/1/22 15:29
 */
public class Res {

    private final static String SUCCESS = "success";

    private final static String FAILED = "failed";

    public static <T> ResResult<T> ok() {
        return new ResResult<>(SUCCESS_CODE, SUCCESS);
    }

    public static <T> ResResult<T> okAndMsg(String message) {
        return new ResResult<>(SUCCESS_CODE, message);
    }

    public static <T> ResResult<T> ok(T data) {
        return new ResResult<>(SUCCESS_CODE, data, SUCCESS);
    }

    public static <T> ResResult<T> error() {
        return new ResResult<>(FAIL_CODE, FAILED);
    }

    public static <T> ResResult<T> error(String message) {
        return new ResResult<>(FAIL_CODE, message);
    }

    public static <T> ResResult<T> response(int code, String msg) {
        return new ResResult<>(code, msg);
    }

    public static <T> ResResult<T> response(int code, String msg, String traceId) {
        return new ErrorResult<>(code, msg, traceId);
    }

    public static <T> ResResult<T> response(int code, String msg, T data) {
        return new ResResult<>(code, data, msg);
    }

}
