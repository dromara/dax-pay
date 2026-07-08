package cn.daxpay.open.channel.leshua.client.req;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import lombok.Data;

/// # 乐刷通道退款请求(主应用侧镜像)
@Data
public class LeshuaRefundReq {
    private LeshuaSdkCredential credential;
    /// 原乐刷订单号
    private String leshuaOrderId;
    /// 商户退款单号
    private String outRefundNo;
    /// 退款金额(单位: 分)
    private Long amount;
    /// 退款原因
    private String reason;
    /// 异步通知地址
    private String notifyUrl;
}
