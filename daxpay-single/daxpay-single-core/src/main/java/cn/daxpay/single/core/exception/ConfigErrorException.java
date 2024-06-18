package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 配置错误
 * @author xxm
 * @since 2024/6/18
 */
public class ConfigErrorException extends PayFailureException{

    public ConfigErrorException(String message) {
        super(DaxPayCommonErrorCode.CONFIG_ERROR,message);
    }

    public ConfigErrorException() {
        super(DaxPayCommonErrorCode.CONFIG_ERROR,"配置错误");
    }
}
