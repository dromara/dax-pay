package cn.daxpay.open.platform.core.enums.pay.trade;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
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
    /// 网关收银台
    CASHIER("cashier"),
    /// 聚合扫码
    AGGRESS_PAY("aggress_pay"),
    /// 易支付协议
    EASY_PAY("easy_pay");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.trade_source";
    }

}
