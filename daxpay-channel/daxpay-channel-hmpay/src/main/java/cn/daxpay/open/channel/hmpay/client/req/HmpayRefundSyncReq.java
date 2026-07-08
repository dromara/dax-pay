package cn.daxpay.open.channel.hmpay.client.req;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import lombok.Data;

/// # 河马付通道退款同步请求(主应用侧)
@Data
public class HmpayRefundSyncReq {

    /// 通道调用凭证
    private HmpaySdkCredential credential;

    /// 原商户订单号
    private String outTradeNo;

    /// 商户退款单号
    private String outRefundNo;
}
