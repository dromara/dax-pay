package cn.daxpay.open.platform.core.enums.pay.notice;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 商户出站通知传输通道
///
/// 字典: notice_transport
/// 与报文格式 [NoticeFormatEnum] 正交: 决定通知如何投递(HTTP 回调 / MQ 推送),
/// 报文内容由 format 决定, 二者组合如 `http+system` / `mq+system`
@Getter
@RequiredArgsConstructor
public enum NoticeTransportEnum implements I18nSupport {

    /// HTTP 异步回调(POST JSON 或 GET query, 由 format 决定)
    HTTP("http"),
    /// MQ 推送(发布到 Artemis Topic, 商户自行订阅消费)
    MQ("mq"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.notice_transport";
    }
}
