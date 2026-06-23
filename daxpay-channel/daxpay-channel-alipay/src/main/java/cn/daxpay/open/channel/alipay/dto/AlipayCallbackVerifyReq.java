package cn.daxpay.open.channel.alipay.dto;

import lombok.Data;

import java.util.Map;

/// # 支付宝通道回调验签请求
///
/// 与子应用 `ChannelCallbackVerifyReq` 字段一致, 通过 Jackson 序列化传输。
/// 主应用收到支付宝异步通知后, 组装本对象调用子应用 `/channel/callback/verify` 完成验签。
@Data
public class AlipayCallbackVerifyReq {

    /// 回调类型(pay-支付回调 / refund-退款回调)
    private String callbackType;

    /// 三方原始通知参数(支付宝 form 参数)
    private Map<String, String> rawParams;

    /// 通道调用配置(验签所需的支付宝公钥等)
    private Map<String, Object> config;
}
