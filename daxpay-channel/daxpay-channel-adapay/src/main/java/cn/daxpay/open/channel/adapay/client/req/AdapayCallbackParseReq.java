package cn.daxpay.open.channel.adapay.client.req;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import lombok.Data;

/// # Adapay 回调验签解析请求(主应用 → 子应用)
///
/// 与子应用 dax-pay-channel-two 的 `AdapayCallbackParseReq` 镜像。
/// 主应用组装凭证(publicKey 为空时子应用用全局平台公钥) + 原始 body 下发, 子应用验签并解析。
@Data
public class AdapayCallbackParseReq {

    /// 通道调用凭证(用于获取平台公钥验签, publicKey 为空时子应用用默认公钥)
    private AdapaySdkCredential credential;

    /// 回调原始 body(Adapay 异步通知 JSON: {data, signature})
    private String body;
}
