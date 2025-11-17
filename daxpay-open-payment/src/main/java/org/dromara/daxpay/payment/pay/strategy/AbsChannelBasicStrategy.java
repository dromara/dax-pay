package org.dromara.daxpay.payment.pay.strategy;

import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;

import java.util.List;

/**
 * 通道基础数据获取策略
 * @author xxm
 * @since 2025/6/4
 */
public abstract class AbsChannelBasicStrategy implements PaymentStrategy{

    /**
     * 获取通道的支付方式列表
     */
    public abstract List<PayMethodEnum> payMethods();

    /**
     * 获取微信类支付类型
     */
    public List<PayMethodEnum> wechatPayMethods() {
        return List.of();
    }

    /**
     * 获取支付宝类支付类型
     */
    public List<PayMethodEnum> alipayPayMethods() {
        return List.of();
    }

    /**
     * 银联类支付方式
     */
    public List<PayMethodEnum> unionPayMethods() {
        return List.of();
    }
}
