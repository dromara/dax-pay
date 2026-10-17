package cn.daxpay.open.channel.jeepay.client.resp;

import lombok.Data;

/// # Jeepay通道退款响应(主应用侧)
///
/// Jeepay 退款为异步受理模式, 受理即成功(PHP 参考语义), `complete` 恒为 true。
@Data
public class JeepayRefundResp {

    /// 商户退款单号(透传 JeepayRefundReq.outRefundNo)
    private String outRefundNo;

    /// Jeepay退款单号(refundOrderId)
    private String tradeNo;

    /// 是否已终态成功(受理即成功模式, 恒为 true)
    private Boolean complete;
}
