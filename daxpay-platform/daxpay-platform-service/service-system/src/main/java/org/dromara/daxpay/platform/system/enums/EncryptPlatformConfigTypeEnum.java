package org.dromara.daxpay.platform.system.enums;

import org.dromara.daxpay.platform.core.exception.BizException;
import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 加密平台配置类型枚举
///
/// 用于定义需要加密存储的敏感配置类型
@Getter
@RequiredArgsConstructor
public enum EncryptPlatformConfigTypeEnum implements I18nSupport {

    /// 对象存储配置
    OSS("oss");

    /// 编码
    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.encrypt_platform_config_type";
    }

    /// 根据编码获取枚举
    public static EncryptPlatformConfigTypeEnum findByCode(String code) {
        return Arrays.stream(values())
            .filter(e -> e.getCode().equals(code))
            .findFirst()
            .orElseThrow(() -> new BizException("error.common.enumUnknown", "EncryptPlatformConfigType"));
    }
}
