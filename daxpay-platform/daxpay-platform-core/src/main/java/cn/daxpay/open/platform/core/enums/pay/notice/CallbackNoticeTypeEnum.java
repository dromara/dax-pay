package cn.daxpay.open.platform.core.enums.pay.notice;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 回调通知类型（已演进为 [NoticeProtocolEnum]）
///
/// @deprecated 使用 [NoticeProtocolEnum]
@Deprecated
@Getter
@RequiredArgsConstructor
public enum CallbackNoticeTypeEnum implements I18nSupport {

    /// 系统回调
    SYSTEM("system");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.callback_notice_type";
    }

}
