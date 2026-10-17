package cn.daxpay.open.channel.yisheng.client.req;

import cn.daxpay.open.channel.yisheng.client.credential.YishengSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 易生通道回调验签解析请求
///
/// 主应用收到易生异步通知(form 提交)后, 将 biz_content 原文与 sign 透传到子应用验签。
@Data
public class YishengCallbackParseReq {

    /// 通道调用凭证(平台公钥验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private YishengSdkCredential credential;

    /// 回调 biz_content 原文(JSON 字符串)
    @NotBlank(message = "{validation.field.rawData.notBlank}")
    private String body;

    /// 回调签名值(sign, 对 biz_content 原文的平台私钥签名)
    @NotBlank(message = "{validation.field.sign.notBlank}")
    private String sign;
}
