package cn.daxpay.open.platform.iam.auth.service.email;

/// # 邮件模板槽位取值
///
/// 五个可编辑槽位(主题/标题/正文/提示/页脚)的一种语言下的一组取值,
/// 取值来源为出厂默认([EmailTemplateEnum])或运营端覆盖(iam_mail_template 表)
///
/// @param subject 邮件主题
/// @param title   邮件标题(正文顶部大标题)
/// @param content 正文文案(纯文本, 支持 {变量} 占位符)
/// @param tip     提示行文案(可空, 空则不输出该行)
/// @param footer  页脚文案(可空, 空则不输出该行)
public record MailSlots(String subject, String title, String content, String tip, String footer) {

    /// 按五槽位构造(枚举出厂默认与覆盖行合并的便捷入口)
    public static MailSlots of(String subject, String title, String content, String tip, String footer) {
        return new MailSlots(subject, title, content, tip, footer);
    }
}
