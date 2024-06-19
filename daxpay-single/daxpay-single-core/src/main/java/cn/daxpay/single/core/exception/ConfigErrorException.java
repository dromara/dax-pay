package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayErrorCode;

/**
 * 配置错误
 * @author xxm
 * @since 2024/6/18
 */
public class ConfigErrorException extends PayFailureException{

    public ConfigErrorException(String message) {
        super(DaxPayErrorCode.CONFIG_ERROR,message);
    }

    public ConfigErrorException() {
        super(DaxPayErrorCode.CONFIG_ERROR,"配置错误");
    }
}
