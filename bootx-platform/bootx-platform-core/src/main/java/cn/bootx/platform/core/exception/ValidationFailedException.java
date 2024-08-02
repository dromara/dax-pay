package cn.bootx.platform.core.exception;

import cn.bootx.platform.core.code.CommonErrorCode;

/**
 * 验证失败异常
 */
public class ValidationFailedException extends BizInfoException {

    public ValidationFailedException(String detail) {
        super(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "验证参数错误" + System.lineSeparator() + detail);
    }

    public ValidationFailedException() {
        super(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,"验证参数错误");
    }

}
