package cn.daxpay.open.channel.douyin.dto;

import lombok.Data;

/// # 抖音通道支付响应
///
/// 与子应用 ChannelPayResp 字段一致
@Data
public class DouyinPayResp {
    private String bizOrderNo;
    private String outOrderNo;
    private String transOrderNo;
    private String payBody;
    private String payBodyType;
    private Boolean complete;
    private String finishTime;
}
