package cn.daxpay.open.channel.jeepay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # Jeepay回调验签解析响应(主应用侧)
///
/// 子应用验签通过后回传标准化业务字段; tradeStatus 为主应用契约标准状态(SUCCESS/FAIL)。
@Data
@Accessors(chain = true)
public class JeepayCallbackParseResp {

    /// 是否验签通过
    private Boolean success;

    /// 回调类型(恒为 PAY)
    private String tradeType;

    /// 商户订单号(mchOrderNo = 主应用支付交易号)
    private String outTradeNo;

    /// Jeepay支付订单号(payOrderId)
    private String tradeNo;

    /// 渠道订单号(channelOrderNo, 微信/支付宝侧单号)
    private String channelOrderNo;

    /// 交易金额(单位: 分)
    private Long amount;

    /// 交易状态(标准化: SUCCESS/FAIL)
    private String tradeStatus;

    /// 完成时间(东八区 OffsetDateTime, successTime 毫秒时间戳换算)
    private OffsetDateTime finishTime;
}
