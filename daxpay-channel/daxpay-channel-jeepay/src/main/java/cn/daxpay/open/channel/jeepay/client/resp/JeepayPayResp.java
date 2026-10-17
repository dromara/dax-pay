package cn.daxpay.open.channel.jeepay.client.resp;

import cn.daxpay.open.channel.jeepay.client.enums.JeepayPayBodyType;
import lombok.Data;

/// # Jeepay通道支付响应(主应用侧)
///
/// Jeepay 统一下单为异步确认模式, 同步不返回终态, `complete` 恒为 false。
@Data
public class JeepayPayResp {

    /// 商户订单号(透传 JeepayPayReq.outTradeNo)
    private String outTradeNo;

    /// Jeepay支付订单号(payOrderId)
    private String tradeNo;

    /// 支付内容(Jeepay payData, 形态由 payBodyType 标识)
    private String payBody;

    /// 支付内容类型(按响应 payDataType 动态判定, none 时为 null)
    private JeepayPayBodyType payBodyType;

    /// 是否已终态成功(Jeepay 下单恒为 false, 等待异步通知确认)
    private Boolean complete;
}
