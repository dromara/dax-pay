package cn.daxpay.open.channel.shouqianba.client.req;

import cn.daxpay.open.channel.shouqianba.client.credential.ShouqianbaSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 收钱吧通道回调验签解析请求(主应用侧)
///
/// 主应用收到收钱吧异步通知(JSON 原文 + authorization 签名头)后透传到子应用验签解析。
@Data
public class ShouqianbaCallbackParseReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private ShouqianbaSdkCredential credential;

    /// 原始回调报文(JSON 字符串, 验签原文)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String body;

    /// authorization 头原串(Base64 编码的 RSA-SHA256 签名)
    @NotBlank(message = "{validation.field.sign.notBlank}")
    private String authorization;
}
