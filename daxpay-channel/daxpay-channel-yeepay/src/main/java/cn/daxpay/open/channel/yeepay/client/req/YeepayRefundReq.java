package cn.daxpay.open.channel.yeepay.client.req;

import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import lombok.Data;

/// # 易宝通道退款请求(主应用 → 子应用)
@Data
public class YeepayRefundReq {

    /// 商户退款单号(易宝 refundRequestId)
    private String outRefundNo;

    /// 原商户订单号(原支付 orderId)
    private String originOutTradeNo;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 退款原因
    private String reason;

    /// 异步通知地址(退款结果通知)
    private String notifyUrl;

    /// 通道调用凭证
    private YeepaySdkCredential credential;
}
