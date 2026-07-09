package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import lombok.Data;

import java.util.Map;

/// # 拉卡拉回调验签解析请求(主应用 → 子应用)
///
/// 与子应用 dax-pay-channel-two 的 `LakalaCallbackParseReq` 镜像。
/// 主应用组装凭证 + 原始 body + header(含 Authorization) 下发, 子应用验签并解析。
@Data
public class LakalaCallbackParseReq {

    /// 通道调用凭证(用于获取拉卡拉公钥证书验签)
    private LakalaSdkCredential credential;

    /// 回调原始 body(拉卡拉异步通知 JSON)
    private String body;

    /// 回调头(需含 Authorization: LKLAPI-SHA256withRSA ...)
    private Map<String, String> headers;
}
