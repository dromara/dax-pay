package cn.daxpay.open.channel.adapay.client.req;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import lombok.Data;

/// # Adapay 回调验签解析请求(主应用 → 子应用)
///
/// 与子应用 dax-pay-channel-two 的 `AdapayCallbackParseReq` 镜像。
/// 主应用从异步通知 HTTP 请求中提取表单参数 data + sign, 连同通道凭证下发, 子应用验签并解析。
///
/// Adapay 异步通知为 application/x-www-form-urlencoded 表单格式, 含两个字段:
/// - data: 业务数据 JSON 明文(待验签内容)
/// - sign: data 的 SHA1withRSA 签名(Base64)
@Data
public class AdapayCallbackParseReq {

    /// 通道调用凭证(用于获取平台公钥验签, publicKey 为空时子应用用默认公钥)
    private AdapaySdkCredential credential;

    /// 回调业务数据 JSON 明文(表单参数 data, 待验签内容)
    private String data;

    /// 回调签名(表单参数 sign, 对 data 的 SHA1withRSA Base64 签名)
    private String sign;
}
