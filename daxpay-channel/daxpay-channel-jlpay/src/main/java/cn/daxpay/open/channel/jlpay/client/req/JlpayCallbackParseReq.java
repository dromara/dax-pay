package cn.daxpay.open.channel.jlpay.client.req;

import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 嘉联通道回调验签解析请求
///
/// 主应用收到嘉联异步通知(JSON body + 签名请求头)后, 将原始报文与签名要素透传到子应用验签。
///
/// 嘉联通知验签串为 `POST\n{回调URI}\n{timestamp}\n{nonce}\n{body}\n`,
/// 其中回调 URI 取嘉联发起通知时的原始请求路径(含 query), 须由主应用原样透传。
@Data
public class JlpayCallbackParseReq {

    /// 通道调用凭证(平台 SM2 公钥验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private JlpaySdkCredential credential;

    /// 原始回调报文(JSON 字符串, 与验签字节一致)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String body;

    /// 回调请求原始 URI(路径 + query, 验签串 apiName 段)
    @NotBlank(message = "{validation.field.callbackPath.notBlank}")
    private String callbackPath;

    /// 回调请求头 x-jlpay-timestamp
    @NotBlank(message = "{validation.field.callbackTimestamp.notBlank}")
    private String timestamp;

    /// 回调请求头 x-jlpay-nonce
    @NotBlank(message = "{validation.field.callbackNonce.notBlank}")
    private String nonce;

    /// 回调请求头 x-jlpay-sign(Base64 签名)
    @NotBlank(message = "{validation.field.callbackSign.notBlank}")
    private String sign;
}
