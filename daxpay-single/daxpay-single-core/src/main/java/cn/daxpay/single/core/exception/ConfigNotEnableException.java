package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 配置未启用
 * @author xxm
 * @since 2024/6/17
 */
public class ConfigNotEnableException extends PayFailureException{

    public ConfigNotEnableException(String message) {
        super(DaxPayCommonErrorCode.CONFIG_NOT_ENABLE,message);
    }

    public ConfigNotEnableException() {
        super(DaxPayCommonErrorCode.CONFIG_NOT_ENABLE,"配置未启用");
    }
}
