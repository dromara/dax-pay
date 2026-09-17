package cn.daxpay.open.channel.sheng.client.req;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import lombok.Data;

/// # 盛付通回调验签解析请求(主应用 → 子应用)
///
/// 与子应用 dax-pay-channel-two 的 `ShengCallbackParseReq` 镜像。
/// 主应用组装凭证 + 回调原始报文(rawBody)下发, 子应用 SHA1withRSA 验签并解析为标准化结果。
@Data
public class ShengCallbackParseReq {

    /// 通道调用凭证(用于取盛付通验签公钥)
    private ShengSdkCredential credential;

    /// 回调原始 body(盛付通异步通知 JSON 原文)
    private String rawBody;
}
