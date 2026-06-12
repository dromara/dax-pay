package org.dromara.daxpay.platform.core.enums.common;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 支付接口枚举
///
@Getter
@RequiredArgsConstructor
public enum PaymentApiEnum implements I18nSupport {

    /// 支付接口
    PAY("pay"),
    /// 退款接口
    REFUND("refund"),
    /// 关闭和撤销接口
    CLOSE("close"),
    /// 转账接口
    TRANSFER("transfer"),
    /// 支付订单查询接口
    PAY_ORDER("payOrder"),
    /// 退款订单查询接口
    REFUND_ORDER("refundOrder"),
    /// 转账订单查询接口
    TRANSFER_ORDER("transferOrder"),
    /// 聚合付款码支付接口
    BAR_PAY("barPay"),
    /// 网关支付链接生成接口
    PRE_PAY("prePay");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.payment_api";
    }

}
