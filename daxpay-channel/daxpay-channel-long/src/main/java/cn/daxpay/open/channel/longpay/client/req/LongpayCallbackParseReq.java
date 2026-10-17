package cn.daxpay.open.channel.longpay.client.req;

import cn.daxpay.open.channel.longpay.client.credential.LongpaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 建行龙支付回调验签解析请求(主应用 → 子应用)
@Data
public class LongpayCallbackParseReq {

    /// 通道调用凭证(建行公钥验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private LongpaySdkCredential credential;

    /// 原始回调报文(urlencoded 表单字符串)
    @NotBlank(message = "{validation.field.callbackBody.notBlank}")
    private String body;
}
