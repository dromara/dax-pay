package cn.daxpay.open.channel.adapay.client.req;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import lombok.Data;

/// # 汇付天下通道关闭订单请求(主应用侧)
@Data
public class AdapayCloseReq {

    /// 商户订单号(回显用)
    private String outTradeNo;

    /// 汇付支付对象 ID
    private String paymentId;

    /// 通道调用凭证
    private AdapaySdkCredential credential;
}
