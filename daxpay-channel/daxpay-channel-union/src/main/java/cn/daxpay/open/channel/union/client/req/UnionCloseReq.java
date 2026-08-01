package cn.daxpay.open.channel.union.client.req;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import lombok.Data;

/// # 云闪付通道关闭订单请求
@Data
public class UnionCloseReq {

    /// 商户订单号
    private String outTradeNo;

    /// 原交易查询凭证(银联 queryId)
    private String queryId;

    /// 支付方式
    private UnionPayMethod method;

    /// 通道调用凭证
    private UnionSdkCredential credential;
}
