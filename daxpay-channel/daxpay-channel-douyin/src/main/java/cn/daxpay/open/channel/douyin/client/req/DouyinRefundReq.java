package cn.daxpay.open.channel.douyin.client.req;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import lombok.Data;

/// # 抖音通道退款请求
@Data
public class DouyinRefundReq {
    /// 原商户订单号
    private String outTradeNo;
    /// 退款单号
    private String outRefundNo;
    /// 退款金额(单位: 分)
    private Long refundAmount;
    /// 原订单总金额(单位: 分)
    private Long totalAmount;
    /// 退款原因
    private String reason;
    /// 退款异步通知地址
    private String notifyUrl;
    /// 通道调用凭证
    private DouyinSdkCredential credential;
}
