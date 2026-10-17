package cn.daxpay.open.channel.cpcn.client.req;

import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 中金通道回调验签解析请求(主应用侧)
///
/// 主应用收到中金异步通知(form 表单 message + signature)后, 将两个字段透传到子应用验签解析。
@Data
public class CpcnCallbackParseReq {

    /// 通道调用凭证(平台证书验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private CpcnSdkCredential credential;

    /// 回调 message 字段(Base64 的明文报文)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String message;

    /// 回调 signature 字段(Base64 的 SM2 签名)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String signature;
}
