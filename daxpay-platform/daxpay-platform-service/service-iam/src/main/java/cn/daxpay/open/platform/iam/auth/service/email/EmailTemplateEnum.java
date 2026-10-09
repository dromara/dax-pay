package cn.daxpay.open.platform.iam.auth.service.email;

import cn.daxpay.open.platform.notify.enums.mail.MailBusinessTypeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/// # 邮箱验证邮件模板类型
///
/// 每个场景携带骨架类型、可用变量声明与五种槽位在中英两种语言下的出厂默认文案;
/// 运营端可在"邮件模板管理"中按场景按语言覆盖默认槽位(存 iam_mail_template 表),
/// 槽位均为纯文本, 样式由 [MailSkeletonEnum] 骨架定死, 不可自定义
@Getter
@RequiredArgsConstructor
public enum EmailTemplateEnum {

    /// 邮箱绑定/换绑验证码
    bindCode("bind-code", MailBusinessTypeEnum.email_bind, MailSkeletonEnum.CODE,
            new MailTemplateVariable[] { MailTemplateVariable.account, MailTemplateVariable.code, MailTemplateVariable.expireMinutes },
            MailSlots.of("邮箱绑定验证码", "邮箱绑定验证",
                    "您正在为账号 {account} 绑定/更换邮箱，验证码为：",
                    "验证码 {expireMinutes} 分钟内有效，且仅可使用一次。如非本人操作，请忽略本邮件。",
                    "本邮件由系统自动发送，请勿直接回复。"),
            MailSlots.of("Email Verification", "Email Verification",
                    "You are binding or changing the email for account {account}. Your verification code is:",
                    "This code expires in {expireMinutes} minutes and can only be used once. If you did not request this, please ignore this email.",
                    "This is an automated message, please do not reply.")),

    /// 找回密码验证码
    resetCode("reset-code", MailBusinessTypeEnum.password_reset, MailSkeletonEnum.CODE,
            new MailTemplateVariable[] { MailTemplateVariable.account, MailTemplateVariable.code, MailTemplateVariable.expireMinutes },
            MailSlots.of("找回密码验证码", "找回密码验证",
                    "您正在重置账号 {account} 的登录密码，验证码为：",
                    "验证码 {expireMinutes} 分钟内有效，且仅可使用一次。如非本人操作，请忽略本邮件并尽快检查账号安全。",
                    "本邮件由系统自动发送，请勿直接回复。"),
            MailSlots.of("Password Reset Verification", "Password Reset Verification",
                    "You are resetting the login password for account {account}. Your verification code is:",
                    "This code expires in {expireMinutes} minutes and can only be used once. If you did not request this, please ignore this email and secure your account.",
                    "This is an automated message, please do not reply.")),

    /// 绑定邮箱变更通知(发送至旧邮箱)
    changeNotice("change-notice", MailBusinessTypeEnum.email_change_notice, MailSkeletonEnum.NOTICE,
            new MailTemplateVariable[] { MailTemplateVariable.account, MailTemplateVariable.newEmail },
            MailSlots.of("绑定邮箱变更通知", "绑定邮箱变更通知",
                    "您的账号 {account} 所绑定的邮箱已由当前邮箱变更为 {newEmail}。",
                    "此后与该账号相关的验证码与通知邮件将发送至新邮箱。如非本人操作，请立即登录修改密码并联系平台管理员。",
                    "本邮件由系统自动发送，请勿直接回复。"),
            MailSlots.of("Account Email Changed", "Account Email Changed",
                    "The email bound to your account {account} has been changed from this address to {newEmail}.",
                    "Verification codes and notifications for this account will be sent to the new email from now on. If you did not make this change, please log in and change your password immediately, then contact the platform administrator.",
                    "This is an automated message, please do not reply.")),

    /// 密码重置成功通知(发送至绑定邮箱)
    resetNotice("reset-notice", MailBusinessTypeEnum.password_reset_notice, MailSkeletonEnum.NOTICE,
            new MailTemplateVariable[] { MailTemplateVariable.account },
            MailSlots.of("密码重置成功通知", "密码重置成功通知",
                    "您的账号 {account} 的登录密码已通过邮箱验证码成功重置，该账号在其他设备上的登录会话已全部退出。",
                    "如非本人操作，请立即联系平台管理员处理。",
                    "本邮件由系统自动发送，请勿直接回复。"),
            MailSlots.of("Password Reset Successfully", "Password Reset Successfully",
                    "The login password of your account {account} has been reset via email verification code, and all login sessions of this account on other devices have been logged out.",
                    "If you did not make this change, please contact the platform administrator immediately.",
                    "This is an automated message, please do not reply.")),

    /// 邮箱解绑验证码(发送至当前绑定邮箱)
    unbindCode("unbind-code", MailBusinessTypeEnum.email_unbind, MailSkeletonEnum.CODE,
            new MailTemplateVariable[] { MailTemplateVariable.account, MailTemplateVariable.code, MailTemplateVariable.expireMinutes },
            MailSlots.of("邮箱解绑验证码", "邮箱解绑验证",
                    "您正在为账号 {account} 解绑邮箱（即本邮箱），验证码为：",
                    "验证码 {expireMinutes} 分钟内有效，且仅可使用一次。如非本人操作，请忽略本邮件并尽快修改登录密码。",
                    "本邮件由系统自动发送，请勿直接回复。"),
            MailSlots.of("Email Unbind Verification", "Email Unbind Verification",
                    "You are unbinding the email (this mailbox) for account {account}. Your verification code is:",
                    "This code expires in {expireMinutes} minutes and can only be used once. If you did not request this, please ignore this email and change your password as soon as possible.",
                    "This is an automated message, please do not reply.")),

    /// 邮箱解绑成功通知(发送至解绑的旧邮箱)
    unbindNotice("unbind-notice", MailBusinessTypeEnum.email_unbind_notice, MailSkeletonEnum.NOTICE,
            new MailTemplateVariable[] { MailTemplateVariable.account },
            MailSlots.of("邮箱解绑通知", "邮箱解绑通知",
                    "您的账号 {account} 所绑定的邮箱（即本邮箱）已解除绑定。",
                    "此后该账号将无法通过本邮箱找回密码。如非本人操作，请立即登录修改密码并联系平台管理员。",
                    "本邮件由系统自动发送，请勿直接回复。"),
            MailSlots.of("Account Email Unbound", "Account Email Unbound",
                    "The email bound to your account {account} (this mailbox) has been unbound.",
                    "This account can no longer use this email for password recovery. If you did not request this, please log in immediately, change your password, and contact the platform administrator.",
                    "This is an automated message, please do not reply."));

    private final String templateName;

    private final MailBusinessTypeEnum businessType;

    private final MailSkeletonEnum skeleton;

    private final MailTemplateVariable[] variables;

    private final MailSlots defaultZh;

    private final MailSlots defaultEn;

    /// 场景中文名(管理端列表展示用)
    public String getName() {
        return this.defaultZh.subject();
    }

    /// 指定语言的出厂默认槽位
    public MailSlots defaultSlots(MailLanguageEnum language) {
        return language == MailLanguageEnum.zh ? this.defaultZh : this.defaultEn;
    }

    /// 按模板编码查找场景(管理端入口用, 不存在返回 null 由调用方报错)
    public static EmailTemplateEnum findByTemplateName(String templateName) {
        for (EmailTemplateEnum item : values()) {
            if (item.templateName.equals(templateName)) {
                return item;
            }
        }
        return null;
    }

    /// 变量示例值(管理端预览渲染用)
    public Map<String, Object> sampleParams(int codeExpireMinutes) {
        return Arrays.stream(this.variables)
                .collect(Collectors.toMap(MailTemplateVariable::getName, v -> switch (v) {
                    case account -> "zhangsan";
                    case code -> "836241";
                    case expireMinutes -> codeExpireMinutes;
                    case newEmail -> "new***@example.com";
                }));
    }
}
