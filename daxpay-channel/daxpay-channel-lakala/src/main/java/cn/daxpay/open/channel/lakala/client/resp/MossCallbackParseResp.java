package cn.daxpay.open.channel.lakala.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 拉卡拉 MOSS 回调验签解析响应(主应用侧, 与子应用镜像)
///
/// responseBody 为子应用按 MOSS 规范构建的签名应答报文,
/// 主应用须将其原样作为 HTTP 响应体返回给拉卡拉。
@Data
@Accessors(chain = true)
public class MossCallbackParseResp {

    /// 是否验签解析成功
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户订单号(支付回调: order_no = 主应用支付交易号; 退款回调: order_no = 退款单号)
    private String outTradeNo;

    /// 通道流水号(支付回调 pay_serial)
    private String tradeNo;

    /// 通道退款流水号(退款回调)
    private String outRefundNo;

    /// 交易状态(抽象态 SUCCESS / FAIL)
    private String tradeStatus;

    /// 完成时间(东八区 OffsetDateTime)
    private OffsetDateTime finishTime;

    /// 应答报文 JSON(签名后, 主应用原样回复给拉卡拉)
    private String responseBody;
}
