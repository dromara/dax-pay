package cn.daxpay.open.platform.notify.enums.mail;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// 邮件业务场景类型
@Getter
@RequiredArgsConstructor
public enum MailBusinessTypeEnum implements I18nSupport {

    /// 平台配置页测试发送
    test("test"),

    /// 运营手动发送(预留)
    manual("manual");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.mail_business_type";
    }
}
