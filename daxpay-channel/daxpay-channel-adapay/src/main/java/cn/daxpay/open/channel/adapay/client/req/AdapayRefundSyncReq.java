package cn.daxpay.open.channel.adapay.client.req;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import lombok.Data;

/// # Adapay 通道退款同步请求(主应用侧)
@Data
public class AdapayRefundSyncReq {

    /// 退款单号(回显用)
    private String outRefundNo;

    /// Adapay 退款对象 ID
    private String refundId;

    /// 通道调用凭证
    private AdapaySdkCredential credential;
}
