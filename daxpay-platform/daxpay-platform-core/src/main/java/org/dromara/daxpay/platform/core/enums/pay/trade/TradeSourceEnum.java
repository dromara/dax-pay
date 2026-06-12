package org.dromara.daxpay.platform.core.enums.pay.trade;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 交易来源
///
@Getter
@RequiredArgsConstructor
public enum TradeSourceEnum implements I18nSupport {

    /// 用户操作
    USER("user"),
    /// 商户API
    MCH_API("mch_api"),
    /// 码牌
    CASHIER_CODE("cashier_code"),
    /// 收银台支付
    CHECKOUT("checkout"),
    /// 聚合/收银台支付
    AGGRESS_PAY("aggress_pay");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.trade_source";
    }

}
