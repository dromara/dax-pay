package cn.daxpay.open.channel.hmpay.client.req;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import lombok.Data;

/// # 河马付通道回调验签解析请求(主应用侧)
///
/// 主应用收到杉德异步通知后, 将原始报文透传到子应用验签(杉德 RSA 验签在子应用侧)。
@Data
public class HmpayCallbackParseReq {

    /// 通道调用凭证(取 publicKey 用于回调验签)
    private HmpaySdkCredential credential;

    /// 原始回调报文(主应用直接转发请求体, form 表单字符串)
    private String body;
}
