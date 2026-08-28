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
    manual("manual"),

    /// 找回密码验证码邮件
    password_reset("password_reset"),

    /// 邮箱绑定/换绑验证码邮件
    email_bind("email_bind"),

    /// 绑定邮箱变更通知(换绑成功后通知旧邮箱)
    email_change_notice("email_change_notice"),

    /// 密码重置成功通知(找回密码重置成功后通知绑定邮箱)
    password_reset_notice("password_reset_notice"),

    /// 邮箱解绑验证码邮件
    email_unbind("email_unbind"),

    /// 邮箱解绑成功通知(解绑后通知旧邮箱)
    email_unbind_notice("email_unbind_notice");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.mail_business_type";
    }
}
