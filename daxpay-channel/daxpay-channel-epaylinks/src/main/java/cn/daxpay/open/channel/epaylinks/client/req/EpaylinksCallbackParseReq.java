package cn.daxpay.open.channel.epaylinks.client.req;

import cn.daxpay.open.channel.epaylinks.client.credential.EpaylinksSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 易票联通道回调验签解析请求(主应用侧)
@Data
public class EpaylinksCallbackParseReq {

    /// 通道调用凭证(平台公钥证书验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private EpaylinksSdkCredential credential;

    /// 原始回调报文(JSON 字符串, 验签对象)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String body;

    /// 回调头 x-efps-sign 签名值(Base64)
    @NotBlank(message = "{validation.field.sign.notBlank}")
    private String sign;
}
