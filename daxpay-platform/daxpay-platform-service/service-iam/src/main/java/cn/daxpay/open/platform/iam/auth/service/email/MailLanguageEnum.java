package cn.daxpay.open.platform.iam.auth.service.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 邮件模板语言
///
/// 邮件文案支持的中英两种语言(中文语种请求发中文模板, 其余一律英文),
/// 取值与 iam_mail_template.language 列一致
@Getter
@RequiredArgsConstructor
public enum MailLanguageEnum {

    /// 中文(简繁统一中文文案)
    zh("zh"),

    /// 英文(中文之外的所有语种)
    en("en");

    private final String code;

    /// 按编码查找语言(不存在返回 null, 由调用方报错)
    public static MailLanguageEnum findByCode(String code) {
        for (MailLanguageEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }
}
