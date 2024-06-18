package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 配置未启用
 * @author xxm
 * @since 2024/6/17
 */
public class ConfigNotEnabledException extends PayFailureException{

    public ConfigNotEnabledException(String message) {
        super(DaxPayCommonErrorCode.CONFIG_NOT_ENABLED,message);
    }

    public ConfigNotEnabledException() {
        super(DaxPayCommonErrorCode.CONFIG_NOT_ENABLED,"配置未启用");
    }
}
