package cn.daxpay.open.channel.leshua.client.req;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import lombok.Data;

/// # 乐刷通道回调验签解析请求(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `LeshuaCallbackParseReq` 镜像, 字段对齐。
/// 主应用收到乐刷异步通知后, 将原始报文透传到子应用验签。
@Data
public class LeshuaCallbackParseReq {
    /// 通道调用凭证(取 tradeKey + signType 用于回调验签)
    private LeshuaSdkCredential credential;
    /// 原始回调报文(XML 字符串)
    private String body;
    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String callbackType;
}
