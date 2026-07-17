package cn.daxpay.open.payment.trade.runtime.service.refund;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 退款建单快照
///
/// 从原支付业务容器([NormalPayOrder] / [GatewayPayOrder])拷贝的字段投影，用于：
/// 1. 填充 [RefundOrder]（通道凭证与业务展示/通知字段）
/// 2. 按 [#product] 选择 [AbsRefundStrategy]
///
/// **不是** [PayRouteService] 的选路结果；退款锚定原支付，禁止再走应用路由。
@Data
@Accessors(chain = true)
public class RefundOrderSnapshot {

    /// 支付产品编码(策略选择，继承自原支付单)
    private String product;

    /// 支付通道(继承自原支付单)
    private String channel;

    /// 通道商户号(继承自原支付单)
    private String channelMchNo;

    /// 支付能力(继承自原支付单)
    private String capability;

    /// 通道应用 AppId（原支付单快照）
    private String channelAppId;

    /// 标题(继承自原支付单)
    private String title;

    /// 商户业务单号(继承自原支付单)
    private String bizOrderNo;

    /// 商户异步通知地址(出站, 非通道回调；继承自原支付单)
    private String notifyUrl;

    /// 客户端 IP(继承自原支付单，可在建单时用当前请求兜底)
    private String clientIp;

    /// 商户附加参数(继承自原支付单)
    private String attach;

    /// 门店号(继承自原支付容器，可空)
    private String storeNo;
}
