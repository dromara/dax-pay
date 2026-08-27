package cn.daxpay.open.platform.notify.enums.mail;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// SMTP 传输加密方式
@Getter
@RequiredArgsConstructor
public enum MailSecurityTypeEnum implements I18nSupport {

    /// 明文不加密(仅内网 SMTP 中继场景)
    none("none"),

    /// STARTTLS 升级加密(常用 587 端口)
    starttls("starttls"),

    /// SSL/TLS 直连加密(常用 465 端口)
    ssl("ssl");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.mail_security_type";
    }

    /// 根据编码获取枚举
    public static MailSecurityTypeEnum findByCode(String code) {
        return Arrays.stream(values())
            .filter(e -> e.getCode().equals(code))
            .findFirst()
            // 通用: 未知的邮件加密方式
            .orElseThrow(() -> new BizException("error.common.enumUnknown", "MailSecurityType"));
    }
}
