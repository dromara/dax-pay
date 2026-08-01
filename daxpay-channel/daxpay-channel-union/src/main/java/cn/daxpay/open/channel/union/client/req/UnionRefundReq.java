package cn.daxpay.open.channel.union.client.req;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import lombok.Data;

/// # 云闪付通道退款请求
@Data
public class UnionRefundReq {

    /// 原商户订单号
    private String outTradeNo;

    /// 原交易查询凭证(银联 origQryId, 必填)
    private String origQueryId;

    /// 退款单号(银联退款 orderId)
    private String outRefundNo;

    /// 退款金额(单位: 分)
    private Long refundAmount;

    /// 退款异步通知地址(银联 backUrl)
    private String notifyUrl;

    /// 支付方式
    private UnionPayMethod method;

    /// 通道调用凭证
    private UnionSdkCredential credential;
}
