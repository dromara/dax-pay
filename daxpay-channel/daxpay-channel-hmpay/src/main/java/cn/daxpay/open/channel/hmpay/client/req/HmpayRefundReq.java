package cn.daxpay.open.channel.hmpay.client.req;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import lombok.Data;

/// # 河马付通道退款请求(主应用侧)
@Data
public class HmpayRefundReq {

    /// 通道调用凭证
    private HmpaySdkCredential credential;

    /// 商户退款单号
    private String outRefundNo;

    /// 原商户订单号
    private String outTradeNo;

    /// 原支付下单时间(yyyyMMddHHmmss)
    private String orderCreateTime;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 退款原因
    private String reason;
}
