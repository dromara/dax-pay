package cn.bootx.platform.core.exception;

import cn.bootx.platform.core.code.CommonCode;
import lombok.Getter;


/**
 * 业务异常基类
 * @see BizErrorException 致命异常 error级别警告
 * @see BizWarnException 业务异常 warn级别
 * @see BizInfoException 哦月异常 info级别
 */
@Getter
public class BizException extends RuntimeException {

    private int code = CommonCode.FAIL_CODE;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message) {
        super(message);
    }

    public BizException() {
    }
}
