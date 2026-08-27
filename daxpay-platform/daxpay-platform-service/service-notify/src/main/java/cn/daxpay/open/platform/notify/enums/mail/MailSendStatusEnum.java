package cn.daxpay.open.platform.notify.enums.mail;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// 邮件发送状态
@Getter
@RequiredArgsConstructor
public enum MailSendStatusEnum implements I18nSupport {

    /// 发送中
    sending("sending"),

    /// 发送成功
    success("success"),

    /// 发送失败
    fail("fail");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.mail_send_status";
    }
}
