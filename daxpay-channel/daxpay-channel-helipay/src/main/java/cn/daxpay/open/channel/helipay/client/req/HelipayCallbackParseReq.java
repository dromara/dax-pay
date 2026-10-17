package cn.daxpay.open.channel.helipay.client.req;

import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import lombok.Data;

import java.util.Map;

/// # 合利宝回调验签解析请求(主应用 → 子应用)
///
/// 主应用接收到合利宝异步通知(form 参数形态)后, 将参数集合(含 sign 字段)连同通道凭证转发到子应用,
/// 由子应用用扫码产品签名密钥按 MD5 值序规则验签(rt1..rt8 固定字段序)。
@Data
public class HelipayCallbackParseReq {

    /// 通道调用凭证(用于获取签名密钥验签)
    private HelipaySdkCredential credential;

    /// 回调参数集合(合利宝异步通知 form 参数, 键值均为字符串, 含 sign 字段)
    private Map<String, String> params;
}
