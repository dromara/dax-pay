package cn.daxpay.open.payment.strategy.pay;

import cn.daxpay.open.payment.common.strategy.PaymentStrategy;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;

/// # 支付关闭策略
///
/// 策略为单例无状态，运行时数据通过方法参数显式传递。
public abstract class AbsPayCloseStrategy implements PaymentStrategy {

    /// 关闭前的处理方式
    public void doBeforeClose(PayTrade trade) {
    }

    /// 关闭操作
    /// @param trade 资金交易凭证
    /// @param useCancel 是否使用撤销方式进行订单关闭
    /// @return 如果执行成功, 返回使用何种方式关闭的支付订单
    public abstract CloseTypeEnum doClose(PayTrade trade, boolean useCancel);

}
