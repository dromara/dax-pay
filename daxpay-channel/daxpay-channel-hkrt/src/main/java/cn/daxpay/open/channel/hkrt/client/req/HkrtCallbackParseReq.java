package cn.daxpay.open.channel.hkrt.client.req;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 海科融通回调验签解析请求(主应用侧, 与子应用镜像)
///
/// 主应用收到海科异步通知后, 将原始报文转发到子应用做验签与字段解析。
@Data
public class HkrtCallbackParseReq {

    /// 通道调用凭证(用 accessKey 验签)
    @NotNull(message = "{validation.field.credential.notNull}")
    private HkrtSdkCredential credential;

    /// 海科回调原始报文(JSON 字符串)
    @NotBlank(message = "{validation.field.rawData.notBlank}")
    private String rawData;

    /// 是否退款回调(true=退款回调, false=支付回调)
    private boolean refund;
}
