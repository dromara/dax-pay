package org.dromara.daxpay.payment.old.pay.channel.dto;

import lombok.Data;

import java.util.Map;

/// # 通道支付请求
///
/// 主项目组装后通过 HTTP 发送给通道适配服务(daxpay-channel-one)的统一下单参数，
/// 字段与通道服务侧 `ChannelPayReq` 保持一致，通过 JSON 序列化进行跨服务通信。
///
@Data
public class ChannelPayReq {

    /// 支付通道编码
    /// @see org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum
    private String channel;

    /// 商户订单号
    private String bizOrderNo;

    /// 支付金额(分)
    private Long amount;

    /// 支付标题
    private String subject;

    /// 支付描述
    private String description;

    /// 支付方式编码(通道侧识别码, 如 alipay_page)
    private String method;

    /// 订单过期时间(通道侧要求的字符串格式)
    private String expireTime;

    /// 其他支付方式(仅 method=other 时生效)
    private String otherMethod;

    /// 通道调用配置(密钥/证书等敏感信息, 由各通道策略组装)
    private Map<String, Object> config;
}
