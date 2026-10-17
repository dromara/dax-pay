package cn.daxpay.open.channel.umfpay.client.req;

import cn.daxpay.open.channel.umfpay.client.credential.UmfpaySdkCredential;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/// # 联动优势通道回调验签解析请求(主应用侧)
///
/// 联动优势异步通知为 GET query 参数(含 sign), 主应用将解码后的参数集合透传到子应用验签解析。
@Data
public class UmfpayCallbackParseReq {

    /// 通道调用凭证(平台公钥验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private UmfpaySdkCredential credential;

    /// 回调 GET 参数集合(已解码, 含 sign)
    @NotNull(message = "{validation.field.callbackBody.notBlank}")
    private Map<String, String> params;
}
