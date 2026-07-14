package cn.daxpay.open.payment.trade.runtime.service.refund;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 退款路由上下文
///
/// 从业务容器([NormalPayOrder] / [GatewayPayOrder])解析出的退款所需路由与业务字段。
@Data
@Accessors(chain = true)
public class RefundRouteContext {

    /// 支付产品编码(策略选择)
    private String product;

    /// 支付通道
    private String channel;

    /// 支付方式
    private String method;

    /// 通道商户号
    private String channelMchNo;

    /// 支付能力
    private String capability;

    /// 通道应用 AppId（原支付单快照）
    private String channelAppId;

    /// 标题
    private String title;

    /// 商户业务单号
    private String bizOrderNo;

    /// 商户异步通知地址(出站, 非通道回调)
    private String notifyUrl;

    /// 客户端 IP
    private String clientIp;

    /// 商户附加参数
    private String attach;
}
