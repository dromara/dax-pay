package cn.bootx.platform.starter.auth.exception;


import cn.bootx.platform.core.exception.BizException;

/**
 * 应用被停用
 *
 * @author xxm
 * @since 2022/6/27
 */
public class ApplicationNotEnableException extends BizException {

    public ApplicationNotEnableException() {
        super("指定应用已被停用");
    }

}
