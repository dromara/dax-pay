package org.dromara.daxpay.platform.common.i18n.util;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/// # 国际化工具类
///
/// 提供静态方法获取翻译文本, 自动从 LocaleContextHolder 获取当前请求的语言
@UtilityClass
public class I18nUtil {

    private MessageSource messageSource;

    /// 设置 MessageSource, 由 I18nAutoConfiguration 在启动时调用
    public void setMessageSource(MessageSource messageSource) {
        I18nUtil.messageSource = messageSource;
    }

    /// 获取翻译文本
    /// @param code 消息 key
    /// @param args 消息参数（可选）
    public String get(String code, Object... args) {
        if (messageSource == null) {
            return code;
        }
        var locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(code, args, code, locale);
        }
        catch (Exception e) {
            return code;
        }
    }

    /// 获取枚举的国际化名称
    /// @param i18nSupport 实现了 I18nSupport 的枚举常量
    public String getEnumName(I18nSupport i18nSupport) {
        if (i18nSupport == null) {
            return null;
        }
        return get(i18nSupport.getI18nPrefix() + "." + i18nSupport.getCode());
    }
}
