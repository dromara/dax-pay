package cn.daxpay.open.channel.fubei.client.req;

import cn.daxpay.open.channel.fubei.client.credential.FubeiSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 付呗通道回调验签解析请求(主应用侧)
///
/// 主应用收到付呗异步通知(form-urlencoded 全参数)后, 将参数集合 JSON 序列化透传到子应用验签解析。
@Data
public class FubeiCallbackParseReq {

    /// 通道调用凭证(app_secret 验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private FubeiSdkCredential credential;

    /// 回调参数集合(JSON 序列化后的 form 参数, 含 sign 与 data)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String body;
}
