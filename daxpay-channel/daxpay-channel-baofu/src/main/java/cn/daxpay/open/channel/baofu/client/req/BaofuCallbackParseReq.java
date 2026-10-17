package cn.daxpay.open.channel.baofu.client.req;

import cn.daxpay.open.channel.baofu.client.credential.BaofuSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 宝付通道回调验签解析请求(主应用侧)
///
/// 主应用收到宝付异步通知(form 提交 dataContent + signStr)后透传到子应用验签解析。
@Data
public class BaofuCallbackParseReq {

    /// 通道调用凭证(平台证书验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private BaofuSdkCredential credential;

    /// 通知业务原文(dataContent, JSON 字符串, 验签串即该原文)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String dataContent;

    /// 通知签名(signStr, hex)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String signStr;
}
