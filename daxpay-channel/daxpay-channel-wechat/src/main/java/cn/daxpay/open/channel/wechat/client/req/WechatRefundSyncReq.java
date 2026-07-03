package cn.daxpay.open.channel.wechat.client.req;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import lombok.Data;

/// # 微信通道退款同步请求
///
/// 与子应用 dax-pay-channel-one 的 `WechatRefundSyncReq` 镜像, 字段对齐。
@Data
public class WechatRefundSyncReq {
    /// 退款单号(对应微信 out_refund_no)
    private String outRefundNo;
    /// 通道调用凭证
    private WechatSdkCredential credential;
}
