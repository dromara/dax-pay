package cn.daxpay.open.channel.mock.client.req;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import lombok.Data;

/// # Mock 通道回调解析请求(主应用 → one)
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.req.MockCallbackParseReq` 镜像。
/// 回调为「JSON 明文单层(event_type/summary/out_biz_no/data)+ Mock-Sign/Mock-Timestamp/Mock-Nonce 三签名头」形态:
/// body 与三头原文原样转发 one 的 mock 模块, 由其用凭证平台公钥验签
/// (签名串 `{timestamp}\n{nonce}\n{body}\n`)后返回
/// [cn.daxpay.open.channel.mock.client.resp.MockCallbackParseResp]。
@Data
public class MockCallbackParseReq {

    /// 通道调用凭证(验签密钥素材)
    private MockSdkCredential credential;

    /// 回调原始 JSON body 原文(明文单层 event_type/data 结构)
    private String body;

    /// 回调签名值(Base64 RSA-SHA256, Mock-Sign 头原值)
    private String sign;

    /// 秒级时间戳(Mock-Timestamp 头原值)
    private String timestamp;

    /// 32 位随机 hex(Mock-Nonce 头原值)
    private String nonce;
}
