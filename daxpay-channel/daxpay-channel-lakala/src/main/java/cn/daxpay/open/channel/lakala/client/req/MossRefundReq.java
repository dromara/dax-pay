package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import lombok.Data;

/// # 拉卡拉 MOSS 退款请求(主应用侧, 与子应用镜像)
@Data
public class MossRefundReq {

    private MossSdkCredential credential;

    /// 商户退款单号(作为 MOSS 退款 order_no)
    private String outRefundNo;

    /// 原商户订单号(原支付 order_no)
    private String originOutTradeNo;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 退款原因
    private String reason;

    /// 客户端IP
    private String clientIp;
}
