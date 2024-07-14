package cn.bootx.platform.core.exception;

/**
 * 普通异常, 不需要显示异常栈
 * @author xxm
 * @since 2024/7/14
 */
public class BizInfoException extends BizException{
    public BizInfoException(int code, String message) {
        super(code,message);
    }

    public BizInfoException(String message) {
        super(message);
    }
}
