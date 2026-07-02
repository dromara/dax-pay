package cn.daxpay.open.payment.common.context;

import cn.daxpay.open.payment.pay.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/// # 普通支付策略执行上下文（请求级，每次支付/同步/关闭创建新实例）
///
/// 显式传递，替代原策略实例字段。持有"容器(业务单) + 资金凭证 + 请求参数"三要素，
/// 与 [PayTrade] 的 containerId 形成冗余双链，使策略无需再自查容器表。
/// 通道配置由 doBeforePay 写入、doPay/doSync/doClose 读取，类型各通道不同。
@Getter
@Setter
@Accessors(chain = true)
public class NormalPayContext {

    /// 普通支付业务单容器（承载 bizOrderNo / 回调地址 / 通道路由等业务信息）
    private NormalPayOrder container;

    /// 资金交易凭证（记录本笔资金动作的状态与通道回执）
    private PayTrade trade;

    /// 普通支付请求参数（pay 流程必填；sync/close 流程可为空）
    private NormalPayParam payParam;

    /// 通道配置（doBeforePay 写入，doPay 读取；类型各通道不同）
    private Object channelConfig;


    @SuppressWarnings("unchecked")
    public <T> T getChannelConfig(Class<T> clazz) {
        return (T) channelConfig;
    }
}
