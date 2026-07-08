package cn.daxpay.open.channel.yeepay.client.req;

import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import lombok.Data;

/// # 易宝通道关闭订单请求(主应用 → 子应用)
@Data
public class YeepayCloseReq {

    /// 商户订单号(支付时传入的 orderId)
    private String outTradeNo;

    /// 通道调用凭证
    private YeepaySdkCredential credential;
}
