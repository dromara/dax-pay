package org.dromara.daxpay.payment.common.strategy;

import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;

/// # 支付相关策略标识接口
///
public interface PaymentStrategy {

    /// 产品枚举
    /// @see ProductEnum
    ProductEnum getProduct();
}

