package cn.daxpay.open.channel.alipay.dto;

import lombok.Data;

/// # 支付宝通道支付响应
///
/// 与子应用 ChannelPayResp 字段一致
@Data
public class AlipayPayResp {
    private String bizOrderNo;
    private String outOrderNo;
    private String transOrderNo;
    private String payBody;
    private String payBodyType;
    private Boolean complete;
    private String finishTime;
}
