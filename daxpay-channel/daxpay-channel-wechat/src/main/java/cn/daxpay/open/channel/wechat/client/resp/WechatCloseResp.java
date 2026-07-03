package cn.daxpay.open.channel.wechat.client.resp;

import lombok.Data;

/// # 微信通道关闭响应
///
/// 与子应用 dax-pay-channel-one 的 `WechatCloseResp` 镜像, 字段对齐。
@Data
public class WechatCloseResp {
    /// 商户订单号(透传 WechatCloseReq.outTradeNo)
    private String outTradeNo;
    /// 微信支付订单号(transaction_id)
    private String transactionId;
}
