package cn.daxpay.open.platform.core.enums.pay.notice;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 商户出站通知 URL 来源
///
/// 字典: notice_source
@Getter
@RequiredArgsConstructor
public enum NoticeSourceEnum implements I18nSupport {

    /// 订单级 notifyUrl
    ORDER("order"),
    /// 应用级订阅配置
    APP("app"),
    /// 协议适配层自带 URL（如易支付）
    PROTOCOL("protocol"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.notice_source";
    }
}
