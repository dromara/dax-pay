package cn.daxpay.open.channel.adapay.client.req;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import lombok.Data;

/// # Adapay 通道支付同步请求(主应用侧)
@Data
public class AdapaySyncReq {

    /// 商户订单号(回显用)
    private String outTradeNo;

    /// Adapay 支付对象 ID
    private String paymentId;

    /// 通道调用凭证
    private AdapaySdkCredential credential;
}
