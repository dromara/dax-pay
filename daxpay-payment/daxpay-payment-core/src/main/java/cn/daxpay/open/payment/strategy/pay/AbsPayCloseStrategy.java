package cn.daxpay.open.payment.strategy.pay;

import cn.daxpay.open.payment.strategy.PaymentStrategy;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;

/// # 支付关闭策略
///
/// 策略为单例无状态，运行时数据通过 [PayStrategyContext] 显式传递。
/// 上下文同时携带容器(业务单)与资金凭证，策略无需再自查容器表。
public abstract class AbsPayCloseStrategy implements PaymentStrategy {

    /// 关闭前的处理方式
    public void doBeforeClose(PayStrategyContext context) {
    }

    /// 关闭操作
    /// @param context 支付上下文（容器 + 资金凭证）
    /// @param useCancel 是否使用撤销方式进行订单关闭
    /// @return 如果执行成功, 返回使用何种方式关闭的支付订单
    public abstract CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel);

}
