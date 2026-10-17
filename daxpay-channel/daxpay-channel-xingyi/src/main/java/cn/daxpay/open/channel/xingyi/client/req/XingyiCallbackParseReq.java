package cn.daxpay.open.channel.xingyi.client.req;

import cn.daxpay.open.channel.xingyi.client.credential.XingyiSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 星驿付通道回调验签解析请求(主应用侧)
///
/// 主应用收到星驿付异步通知(JSON 报文)后, 将原始报文透传到子应用用商户公钥验签解析。
@Data
public class XingyiCallbackParseReq {

    /// 通道调用凭证(用于获取商户公钥验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private XingyiSdkCredential credential;

    /// 原始回调报文(JSON 字符串)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String body;
}
