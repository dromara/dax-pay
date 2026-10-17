package cn.daxpay.open.channel.llianpay.client.req;

import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 连连通道回调验签解析请求(主应用侧)
///
/// 主应用收到连连异步通知(JSON body)后, 将原始报文与回调头 signature-data 签名值透传到子应用验签。
@Data
public class LlianpayCallbackParseReq {

    /// 通道调用凭证(连连平台公钥验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private LlianpaySdkCredential credential;

    /// 原始回调报文(JSON 字符串, 验签对象)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String body;

    /// 回调头 signature-data 签名值(Base64)
    @NotBlank(message = "{validation.field.sign.notBlank}")
    private String sign;
}
