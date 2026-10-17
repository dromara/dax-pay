package cn.daxpay.open.channel.jeepay.client.req;

import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import lombok.Data;

import java.util.Map;

/// # Jeepay回调验签解析请求(主应用侧)
///
/// 主应用接收到 Jeepay 异步通知(form 参数形态)后, 将参数集合(含 sign 字段)连同通道凭证转发到子应用验签解析。
@Data
public class JeepayCallbackParseReq {

    /// 通道调用凭证(用于获取应用密钥验签)
    private JeepaySdkCredential credential;

    /// 回调参数集合(Jeepay 异步通知 form 参数, 键值均为字符串, 含 sign 字段)
    private Map<String, String> params;
}
