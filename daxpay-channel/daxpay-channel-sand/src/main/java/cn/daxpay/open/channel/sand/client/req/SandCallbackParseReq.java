package cn.daxpay.open.channel.sand.client.req;

import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 杉德通道回调验签解析请求(主应用 → 子应用)
///
/// 主应用收到杉德异步通知(表单 bizData + sign)后, 将原始字段透传到子应用验签解析。
@Data
public class SandCallbackParseReq {

    /// 通道调用凭证(杉德公钥证书验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private SandSdkCredential credential;

    /// 通知 bizData 明文 JSON 字符串
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String bizData;

    /// 通知签名(杉德私钥对 bizData 的 SHA256WithRSA 签名)
    @NotBlank(message = "{validation.field.sign.notBlank}")
    private String sign;
}
