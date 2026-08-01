package cn.daxpay.open.channel.union.client.req;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import lombok.Data;

/// # 云闪付通道支付同步请求
@Data
public class UnionSyncReq {

    /// 商户订单号(银联 orderId)
    private String outTradeNo;

    /// 支付方式
    private UnionPayMethod method;

    /// 通道调用凭证
    private UnionSdkCredential credential;
}
