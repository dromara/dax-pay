package cn.daxpay.open.channel.wechat.client.req;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import lombok.Data;

/// # 微信回调验签解析请求
///
/// 主应用接收到微信异步通知后, 将原始 header + body 连同通道凭证转发到子应用,
/// 由子应用使用微信 SDK [NotificationParser] 完成验签与 AES 解密,
/// 返回结构化的回调业务数据 [cn.daxpay.open.channel.wechat.client.resp.WechatCallbackParseResp]。
@Data
public class WechatCallbackParseReq {

    /// 通道调用凭证
    private WechatSdkCredential credential;

    /// 回调原始 body(微信 POST 的 JSON 密文)
    private String body;

    /// 回调头: Wechatpay-Serial(平台证书序列号 / 支付公钥ID)
    private String serial;

    /// 回调头: Wechatpay-Nonce(随机串)
    private String nonce;

    /// 回调头: Wechatpay-Signature(签名)
    private String signature;

    /// 回调头: Wechatpay-Timestamp(时间戳)
    private String timestamp;
}
