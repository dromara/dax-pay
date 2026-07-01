package cn.daxpay.open.payment.common.context;

import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/// # 支付策略执行上下文（请求级，每次支付创建新实例）
///
/// 显式传递，替代原 AbsPayStrategy 实例字段。通道配置由 doBeforePay 写入、doPay 读取。
@Getter
@Setter
@Accessors(chain = true)
public class PayStrategyContext {

    /// 支付参数
    private NormalPayParam payParam;

    /// 资金交易凭证
    private PayTrade trade;

    /// 通道配置（doBeforePay 写入，doPay 读取；类型各通道不同）
    private Object channelConfig;

    public PayStrategyContext() {
    }

    public PayStrategyContext(NormalPayParam payParam) {
        this.payParam = payParam;
    }

    /// 读取通道配置（各通道策略 cast 自己的配置类型）
    @SuppressWarnings("unchecked")
    public <T> T getChannelConfig() {
        return (T) channelConfig;
    }
}
