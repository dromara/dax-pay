package org.dromara.daxpay.platform.core.enums.pay.trade;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 交易流水记录类型
///
/// 字典: trade_flow_type
@Getter
@RequiredArgsConstructor
public enum TradeFlowTypeEnum implements I18nSupport {

    /// 支付
    PAY("pay"),
    /// 退款
    REFUND("refund"),
    /// 转账
    TRANSFER("transfer");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.trade_flow_type";
    }

}
