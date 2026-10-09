package cn.daxpay.open.platform.iam.auth.service.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 邮件模板占位符变量
///
/// 各邮件场景文案中可引用的变量, 以 `{变量名}` 形式书写,
/// 渲染时替换为实际值(展示形态由骨架决定, 详见 [MailSkeletonEnum])
@Getter
@RequiredArgsConstructor
public enum MailTemplateVariable {

    /// 用户账号
    account("account", "用户账号"),

    /// 验证码(验证码型骨架中以大字号样式独立展示)
    code("code", "验证码(以大字号样式独立展示)"),

    /// 验证码有效分钟数
    expireMinutes("expireMinutes", "验证码有效分钟数"),

    /// 新绑定邮箱地址
    newEmail("newEmail", "新绑定邮箱");

    private final String name;

    /// 变量说明(管理端提示用)
    private final String desc;
}
