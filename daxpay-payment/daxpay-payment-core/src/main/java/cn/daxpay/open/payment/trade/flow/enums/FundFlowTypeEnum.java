package cn.daxpay.open.payment.trade.flow.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 资金流水类型
///
/// 字典: fund_flow_type
@Getter
@RequiredArgsConstructor
public enum FundFlowTypeEnum implements I18nSupport {

    /// 收款(支付成功入账)
    PAY("pay"),
    /// 退款(退款成功支出)
    REFUND("refund"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.fund_flow_type";
    }
}
