package cn.daxpay.open.payment.strategy.pay;

import cn.daxpay.open.payment.common.context.NormalPayContext;
import cn.daxpay.open.payment.common.strategy.PaymentStrategy;
import cn.daxpay.open.payment.pay.bo.PayTradeResultBo;

/// # 抽象普通支付策略基类
///
/// 策略为单例无状态，运行时数据通过 [NormalPayContext] 显式传递。
/// 上下文同时携带容器(业务单)与资金凭证，策略无需再自查容器表。
public abstract class AbsNormalPayStrategy implements PaymentStrategy {

    /// 支付前处理 包含必要的校验以及对当前通道支付配置信息的初始化
    /// 出现错误不会保存相关信息
    public void doBeforePay(NormalPayContext context) {
    }

    /// 支付操作
    /// 出现错误会保存相关信息
    public abstract PayTradeResultBo doPay(NormalPayContext context);

}
