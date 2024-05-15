package cn.bootx.platform.common.core.exception;

import java.io.Serializable;

import static cn.bootx.platform.common.core.code.CommonErrorCode.VALIDATE_PARAMETERS_ERROR;

/**
 * 验证失败异常
 */
public class ValidationFailedException extends BizException implements Serializable {

    private static final long serialVersionUID = -3973809880035275189L;

    public ValidationFailedException(String detail) {
        super(VALIDATE_PARAMETERS_ERROR, "验证参数错误" + System.lineSeparator() + detail);
    }

    public ValidationFailedException() {
        super();
    }

}
