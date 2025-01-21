package cn.bootx.platform.core.exception;

/**
 * 致命异常
 * @author xxm
 * @since 2024/7/14
 */
public class BizErrorException extends BizException {
    public BizErrorException(int code, String message) {
        super(code,message);
    }

    public BizErrorException(String message) {
        super(message);
    }
}
