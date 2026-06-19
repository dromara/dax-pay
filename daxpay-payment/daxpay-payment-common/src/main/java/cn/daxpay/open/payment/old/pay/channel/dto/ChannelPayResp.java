package cn.daxpay.open.payment.old.pay.channel.dto;

import lombok.Data;

/// # 通道支付响应
///
/// 通道适配服务处理完成后返回的统一下单结果，
/// 字段与通道服务侧 `ChannelPayResp` 保持一致。
///
@Data
public class ChannelPayResp {

    /// 商户订单号
    private String bizOrderNo;

    /// 通道订单号
    private String outOrderNo;

    /// 透传订单号(第三方交易号)
    private String transOrderNo;

    /// 支付参数体(表单/二维码内容/订单串等)
    private String payBody;

    /// 支付参数体类型(form/order_id/qr_code 等)
    private String payBodyType;

    /// 是否支付完成(付款码等同步支付场景为 true)
    private Boolean complete;

    /// 支付完成时间
    private String finishTime;
}
