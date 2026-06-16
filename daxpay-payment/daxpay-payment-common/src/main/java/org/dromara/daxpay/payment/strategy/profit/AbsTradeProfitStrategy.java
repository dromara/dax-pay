package org.dromara.daxpay.payment.strategy.profit;

import org.dromara.daxpay.payment.common.strategy.PaymentStrategy;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrderExpand;

/// # 交易订单费率计算策略抽象类
///
public abstract class AbsTradeProfitStrategy implements PaymentStrategy {

    /// 获取所适用的费率产品, 默认返回支付渠道,
    /// 费率产品必须要为下划线分隔格式, 否则映射会有问题
    public String getRateProduct(PayOrder order, PayOrderExpand payOrderExpand) {
        return order.getProvider();
    }

}

