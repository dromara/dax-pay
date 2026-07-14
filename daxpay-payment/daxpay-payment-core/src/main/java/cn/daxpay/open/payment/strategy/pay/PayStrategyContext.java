package cn.daxpay.open.payment.strategy.pay;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/// # 支付策略执行上下文(请求级,显式传参)
///
/// 持有单笔支付策略管线的"资金凭证 + 通道配置 + 路由参数"三要素,
/// 每次支付/同步/关闭创建新实例,显式传递,不进 ThreadLocal。
///
/// close/sync 策略直接从本类读取路由参数(channelMchNo / capability / clientIp),
/// 这些字段由 service 层从容器提取后填入。
///
/// 与线程级身份上下文严格区分:
/// - 本类 = 请求级数据载体(函数传参)
/// - `common.context.PaymentContext` = 线程级身份(商户号)
@Getter
@Setter
@Accessors(chain = true)
public class PayStrategyContext {

    /// 资金交易凭证(记录本笔资金动作的状态与通道回执)
    private PayTrade trade;

    /// 普通支付请求参数(pay 流程必填;sync/close 流程可为空)
    private NormalPayParam payParam;

    /// 通道配置(doBeforePay 写入,doPay 读取;类型各通道不同)
    private Object channelConfig;

    /// 通道商户号(service 层从容器提取填入,供 close/sync 策略组装凭证)
    private String channelMchNo;

    /// 支付能力编码(service 层从容器提取填入,供 close/sync 策略组装凭证)
    private String capability;

    /// 客户端 IP(service 层从容器提取填入,供 close/sync 策略使用)
    private String clientIp;

    @SuppressWarnings("unchecked")
    public <T> T getChannelConfig(Class<T> clazz) {
        return (T) channelConfig;
    }
}
