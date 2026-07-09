package cn.daxpay.open.channel.vbill.client.req;

import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import lombok.Data;

/// # 随行付回调验签解析请求(主应用 → 子应用)
///
/// 与子应用 dax-pay-channel-two 的 `VbillCallbackParseReq` 镜像。
/// 主应用组装凭证 + 原始 body 下发, 子应用用天阙公钥 SHA1withRSA 验签并解析。
@Data
public class VbillCallbackParseReq {

    /// 通道调用凭证(用于获取天阙公钥验签)
    private VbillSdkCredential credential;

    /// 回调原始 body(随行付异步通知 JSON)
    private String body;
}
