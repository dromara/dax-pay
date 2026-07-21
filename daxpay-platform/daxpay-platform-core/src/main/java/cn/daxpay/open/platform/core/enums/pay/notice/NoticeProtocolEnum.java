package cn.daxpay.open.platform.core.enums.pay.notice;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 商户出站通知协议
///
/// 字典: notice_protocol
/// 演进自原 CallbackNoticeTypeEnum，表示报文协议而非「回调/订阅」双轨
@Getter
@RequiredArgsConstructor
public enum NoticeProtocolEnum implements I18nSupport {

    /// 标准 DaxPay 签名 JSON 回调
    SYSTEM("system"),
    /// 易支付兼容协议 GET 回调
    EASY_PAY("easy_pay"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.notice_protocol";
    }
}
