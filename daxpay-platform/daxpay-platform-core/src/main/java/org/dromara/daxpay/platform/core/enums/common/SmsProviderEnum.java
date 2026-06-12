package org.dromara.daxpay.platform.core.enums.common;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;

/// # 短信供应商枚举
///
@Getter
@RequiredArgsConstructor
public enum SmsProviderEnum implements I18nSupport {

    /// 阿里云
    ALIYUN("aliyun"),
    /// 腾讯云
    TENCENT("tencent");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.sms_provider";
    }
    public static SmsProviderEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(value -> value.code.equals(code))
                .findFirst()
                .orElse(null);
    }

}
