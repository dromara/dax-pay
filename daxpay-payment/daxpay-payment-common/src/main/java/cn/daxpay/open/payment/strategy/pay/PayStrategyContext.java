package cn.daxpay.open.payment.strategy.pay;

import cn.daxpay.open.payment.pay.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/// # 支付策略执行上下文(请求级,显式传参)
///
/// 持有单笔支付策略管线的"容器(业务单) + 资金凭证 + 请求参数 + 通道配置"四要素,
/// 每次支付/同步/关闭创建新实例,显式传递,不进 ThreadLocal。
///
/// 与线程级身份上下文严格区分:
/// - 本类 = 请求级数据载体(函数传参)
/// - `runtime.PaymentContext` = 线程级身份(商户号)
@Getter
@Setter
@Accessors(chain = true)
public class PayStrategyContext {

    /// 普通支付业务单容器(承载 bizOrderNo / 回调地址 / 通道路由等业务信息)
    private NormalPayOrder container;

    /// 资金交易凭证(记录本笔资金动作的状态与通道回执)
    private PayTrade trade;

    /// 普通支付请求参数(pay 流程必填;sync/close 流程可为空)
    private NormalPayParam payParam;

    /// 通道配置(doBeforePay 写入,doPay 读取;类型各通道不同)
    private Object channelConfig;


    @SuppressWarnings("unchecked")
    public <T> T getChannelConfig(Class<T> clazz) {
        return (T) channelConfig;
    }
}
