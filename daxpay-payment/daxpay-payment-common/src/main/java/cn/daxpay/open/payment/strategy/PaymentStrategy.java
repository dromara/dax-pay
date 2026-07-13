package cn.daxpay.open.payment.strategy;

import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;

/// # 支付相关策略标识接口
///
public interface PaymentStrategy {

    /// 产品枚举
    /// @see ProductEnum
    ProductEnum getProduct();
}

