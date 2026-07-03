package cn.daxpay.open.channel.wechat.client.req;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import lombok.Data;

/// # 微信通道同步请求
///
/// 与子应用 dax-pay-channel-one 的 `WechatSyncReq` 镜像, 字段对齐。
@Data
public class WechatSyncReq {
    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;
    /// 微信支付订单号(transaction_id, 可选)
    private String transactionId;
    /// 通道调用凭证
    private WechatSdkCredential credential;
}
