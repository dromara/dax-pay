package org.dromara.daxpay.payment.channel.strategy;

import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;

/// # 服务商策略基类
///
public interface OnbStrategy {

    /// 产品枚举
    /// @see ProductEnum
    ProductEnum getProduct();
}

