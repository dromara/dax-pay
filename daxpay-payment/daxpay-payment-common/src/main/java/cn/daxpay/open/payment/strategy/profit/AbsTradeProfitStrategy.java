package cn.daxpay.open.payment.strategy.profit;

import cn.daxpay.open.payment.common.strategy.PaymentStrategy;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrderExpand;

/// # 交易订单费率计算策略抽象类
///
public abstract class AbsTradeProfitStrategy implements PaymentStrategy {

    /// 获取所适用的费率产品, 默认返回支付渠道,
    /// 费率产品必须要为下划线分隔格式, 否则映射会有问题
    public String getRateProduct(PayOrder order, PayOrderExpand payOrderExpand) {
        return order.getProvider();
    }

}

