package cn.daxpay.open.channel.union.client.req;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import lombok.Data;

/// # 云闪付通道支付请求
///
/// 与子应用镜像, 主应用经声明式 HTTP 客户端转发。
@Data
public class UnionPayReq {

    /// 商户订单号(主应用支付交易号, 作为银联 orderId)
    private String outTradeNo;

    /// 订单金额(单位: 分, 银联 txnAmt)
    private Long amount;

    /// 商品描述(银联 orderDesc)
    private String description;

    /// 支付方式
    private UnionPayMethod method;

    /// 异步通知地址(银联 backUrl)
    private String notifyUrl;

    /// 付款码(被扫 BARCODE 必填, 银联 qrNo)
    private String authCode;

    /// 客户端 IP
    private String clientIp;

    /// 通道调用凭证
    private UnionSdkCredential credential;
}
