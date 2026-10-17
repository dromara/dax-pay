package cn.daxpay.open.channel.kuaiqian.client.req;

import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 快钱通道回调验签解析请求(主应用侧)
///
/// 主应用收到快钱异步通知(JSON + 数字信封)后, 将原始报文透传到子应用解密验签。
@Data
public class KuaiqianCallbackParseReq {

    /// 通道调用凭证(商户 pfx 解密 + 平台证书验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private KuaiqianSdkCredential credential;

    /// 原始回调报文(JSON 字符串)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String body;
}
