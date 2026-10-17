package cn.daxpay.open.channel.yizhifubj.client.req;

import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 一直付通道回调验签解析请求(主应用侧)
///
/// 主应用收到一直付异步通知(body `{"data": 密文}` + 请求头 encryptKey)后,
/// 将原始报文与请求头透传到子应用解密验签。
@Data
public class YizhifubjCallbackParseReq {

    /// 通道调用凭证(商户 pfx 解密 + 平台证书验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private YizhifubjSdkCredential credential;

    /// 原始回调报文(body JSON 字符串, 含 data 密文)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String body;

    /// 回调请求头 encryptKey(AES 密钥密文, 商户 pfx 私钥解密)
    @NotBlank(message = "{validation.field.sign.notBlank}")
    private String encryptKey;
}
