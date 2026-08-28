package cn.daxpay.open.platform.iam.auth.service.email;

import cn.daxpay.open.platform.notify.enums.mail.MailBusinessTypeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 邮箱验证邮件模板类型
///
/// 主题文案为邮件内部双语文案(不走 i18n 词条体系, 按请求语言在中英间二选一),
/// 模板文件位于 resources/mail/{templateName}.{zh|en}.html
@Getter
@RequiredArgsConstructor
public enum EmailTemplateEnum {

    /// 邮箱绑定/换绑验证码
    bindCode("bind-code", MailBusinessTypeEnum.email_bind, "邮箱绑定验证码", "Email Verification"),

    /// 找回密码验证码
    resetCode("reset-code", MailBusinessTypeEnum.password_reset, "找回密码验证码", "Password Reset Verification"),

    /// 绑定邮箱变更通知(发送至旧邮箱)
    changeNotice("change-notice", MailBusinessTypeEnum.email_change_notice, "绑定邮箱变更通知", "Account Email Changed"),

    /// 密码重置成功通知(发送至绑定邮箱)
    resetNotice("reset-notice", MailBusinessTypeEnum.password_reset_notice, "密码重置成功通知", "Password Reset Successfully"),

    /// 邮箱解绑验证码(发送至当前绑定邮箱)
    unbindCode("unbind-code", MailBusinessTypeEnum.email_unbind, "邮箱解绑验证码", "Email Unbind Verification"),

    /// 邮箱解绑成功通知(发送至解绑的旧邮箱)
    unbindNotice("unbind-notice", MailBusinessTypeEnum.email_unbind_notice, "邮箱解绑通知", "Account Email Unbound");

    private final String templateName;

    private final MailBusinessTypeEnum businessType;

    private final String subjectZh;

    private final String subjectEn;
}
