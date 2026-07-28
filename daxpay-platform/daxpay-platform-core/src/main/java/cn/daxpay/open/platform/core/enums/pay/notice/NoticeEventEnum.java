package cn.daxpay.open.platform.core.enums.pay.notice;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 商户出站通知事件
///
/// 字典: notice_event
/// 与 [TradeTypeEnum] 解耦: 事件码为 `域.动作`，订阅配置可用前缀匹配（如 `pay` 匹配所有 `pay.*`）
@Getter
@RequiredArgsConstructor
public enum NoticeEventEnum implements I18nSupport {

    /// 支付成功
    PAY_SUCCESS("pay.success"),
    /// 支付失败
    PAY_FAIL("pay.fail"),
    /// 支付关闭
    PAY_CLOSE("pay.close"),
    /// 退款成功
    REFUND_SUCCESS("refund.success"),
    /// 退款失败
    REFUND_FAIL("refund.fail"),
    /// 退款关闭
    REFUND_CLOSE("refund.close"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.notice_event";
    }

    /// 事件所属业务前缀（pay / refund）
    public String prefix() {
        int dot = code.indexOf('.');
        return dot > 0 ? code.substring(0, dot) : code;
    }
}
