package cn.daxpay.open.channel.adapay.client.req;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import lombok.Data;

/// # 汇付天下通道退款请求(主应用侧)
@Data
public class AdapayRefundReq {

    /// 原商户订单号(回显用)
    private String outTradeNo;

    /// 原汇付支付对象 ID
    private String paymentId;

    /// 退款单号(主应用退款单号, 作为汇付退款 refund_order_no)
    private String outRefundNo;

    /// 退款金额(单位: 分)
    private Long refundAmount;

    /// 退款原因
    private String reason;

    /// 退款异步通知地址
    private String notifyUrl;

    /// 通道调用凭证
    private AdapaySdkCredential credential;
}
