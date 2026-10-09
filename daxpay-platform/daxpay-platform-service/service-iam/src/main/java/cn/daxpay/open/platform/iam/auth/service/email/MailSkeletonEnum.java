package cn.daxpay.open.platform.iam.auth.service.email;

/// # 邮件骨架类型
///
/// 邮件 HTML 骨架(样式定死)的两种形态:
/// 验证码型在正文之后多一个验证码大字块行, 其余结构一致
public enum MailSkeletonEnum {

    /// 验证码型: 品牌头 + 标题 + 正文 + 验证码大字块 + 提示行 + 页脚
    CODE,

    /// 通知型: 品牌头 + 标题 + 正文 + 提示行 + 页脚
    NOTICE
}
