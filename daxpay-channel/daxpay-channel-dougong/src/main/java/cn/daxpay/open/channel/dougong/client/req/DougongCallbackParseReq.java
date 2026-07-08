package cn.daxpay.open.channel.dougong.client.req;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import lombok.Data;

/// # 斗拱通道回调验签解析请求(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `DougongCallbackParseReq` 镜像, 字段对齐。
/// 主应用收到汇付异步通知后, 将原始报文透传到子应用验签(汇付 RsaUtils 验签在子应用侧)。
@Data
public class DougongCallbackParseReq {

    /// 通道调用凭证(取 dgPublicKey 用于回调验签)
    private DougongSdkCredential credential;

    /// 原始回调报文(主应用直接转发请求体, 通常为 JSON 字符串)
    private String body;
}
