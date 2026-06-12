package org.dromara.daxpay.platform.system.enums;

import org.dromara.daxpay.platform.core.exception.BizException;
import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// 用户协议内容格式
@Getter
@RequiredArgsConstructor
public enum UserProtocolContentFormatEnum implements I18nSupport {

    /// Markdown
    MARKDOWN("MARKDOWN");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.user_protocol_content_format";
    }

    public static UserProtocolContentFormatEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new BizException("error.common.enumUnknown", "UserProtocolContentFormat"));
    }
}
