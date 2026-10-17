package cn.daxpay.open.channel.duolabao.client.req;

import cn.daxpay.open.channel.duolabao.client.credential.DuolabaoSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 哆啦宝通道回调验签解析请求(主应用侧)
///
/// 主应用收到哆啦宝异步通知(JSON 报文 + Header timestamp/token)后,
/// 将原始报文与验签 Header 一起透传到子应用验签解析。
@Data
public class DuolabaoCallbackParseReq {

    /// 通道调用凭证(验签需 secretKey)
    @NotNull(message = "{validation.field.credential.notNull}")
    private DuolabaoSdkCredential credential;

    /// 原始回调报文(JSON 字符串, 验签原文)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String body;

    /// 回调 Header 携带的 timestamp
    @NotBlank(message = "{validation.field.callbackTimestamp.notBlank}")
    private String notifyTimestamp;

    /// 回调 Header 携带的 token(待比对签名)
    @NotBlank(message = "{validation.field.callbackSign.notBlank}")
    private String notifyToken;
}
