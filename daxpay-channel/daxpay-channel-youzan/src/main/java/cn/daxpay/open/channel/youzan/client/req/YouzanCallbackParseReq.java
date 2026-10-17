package cn.daxpay.open.channel.youzan.client.req;

import cn.daxpay.open.channel.youzan.client.credential.YouzanSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 有赞通道回调验签解析请求
///
/// 主应用收到有赞异步通知(JSON body + 请求头验签材料)后, 将原始报文与头透传到子应用验签。
@Data
public class YouzanCallbackParseReq {

    /// 通道调用凭证(平台公钥验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private YouzanSdkCredential credential;

    /// 原始回调报文(JSON 字符串)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String body;

    /// 回调签名值(请求头 youzan-sign)
    private String sign;

    /// 回调时间戳(请求头 youzan-timestamp)
    private String timestamp;

    /// 回调随机串(请求头 youzan-noncestr)
    private String noncestr;
}
