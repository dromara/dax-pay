package cn.daxpay.open.channel.union.client.req;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import lombok.Data;

/// # 云闪付通道退款同步请求
@Data
public class UnionRefundSyncReq {

    /// 退款单号(银联退款 orderId)
    private String outRefundNo;

    /// 支付方式
    private UnionPayMethod method;

    /// 通道调用凭证
    private UnionSdkCredential credential;
}
