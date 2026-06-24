package cn.daxpay.open.platform.notify.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// 公告状态
@Getter
@RequiredArgsConstructor
public enum NotifyStatusEnum implements I18nSupport {

    /// 草稿
    draft("draft"),

    /// 发布
    published("published"),

    /// 下线
    offline("offline");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.notify_status";
    }
}
