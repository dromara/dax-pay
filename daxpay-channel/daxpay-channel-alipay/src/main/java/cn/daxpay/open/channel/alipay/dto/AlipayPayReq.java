package cn.daxpay.open.channel.alipay.dto;

import lombok.Data;

import java.util.Map;

/// # 支付宝通道支付请求
///
/// 与子应用 ChannelPayReq 字段一致, 通过 Jackson 序列化传输
@Data
public class AlipayPayReq {
    private String channel;
    private String bizOrderNo;
    private Long amount;
    private String subject;
    private String description;
    private String method;
    private String expireTime;
    private String otherMethod;
    private Map<String, Object> config;
}
