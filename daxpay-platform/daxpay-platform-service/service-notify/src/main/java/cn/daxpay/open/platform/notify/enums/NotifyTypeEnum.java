package cn.daxpay.open.platform.notify.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// 通知类型(用于 SSE 推送载荷与前端分桶)
@Getter
@RequiredArgsConstructor
public enum NotifyTypeEnum implements I18nSupport {

    /// 公告(广播)
    notice("notice"),

    /// 个人消息(定向)
    message("message");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.notify_type";
    }
}
