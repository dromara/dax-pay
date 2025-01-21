package cn.bootx.platform.core.exception;

/**
 * 警告异常
 * @author xxm
 * @since 2024/7/14
 */
public class BizWarnException extends BizException{

    public BizWarnException(int code, String message) {
        super(code,message);
    }

    public BizWarnException(String message) {
        super(message);
    }
}
