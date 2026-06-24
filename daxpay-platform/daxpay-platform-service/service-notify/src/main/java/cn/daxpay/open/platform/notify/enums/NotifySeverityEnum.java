package cn.daxpay.open.platform.notify.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// 公告重要程度
@Getter
@RequiredArgsConstructor
public enum NotifySeverityEnum implements I18nSupport {

    /// 普通
    normal("normal"),

    /// 重要
    important("important");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.notify_severity";
    }
}
