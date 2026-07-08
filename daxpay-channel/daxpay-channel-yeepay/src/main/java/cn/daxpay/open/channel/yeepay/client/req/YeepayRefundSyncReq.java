package cn.daxpay.open.channel.yeepay.client.req;

import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import lombok.Data;

/// # 易宝通道退款查询请求(主应用 → 子应用)
@Data
public class YeepayRefundSyncReq {

    /// 原商户订单号(原支付 orderId)
    private String originOutTradeNo;

    /// 商户退款单号(易宝 refundRequestId)
    private String outRefundNo;

    /// 通道调用凭证
    private YeepaySdkCredential credential;
}
