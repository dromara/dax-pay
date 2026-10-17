package cn.daxpay.open.channel.shouqianba.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 收钱吧通道回调验签解析响应(主应用侧)
///
/// 仅承载支付回调(收钱吧退款为同步终态, 无退款回调)。
@Data
@Accessors(chain = true)
public class ShouqianbaCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 商户订单号(支付时上送的 client_sn = 主平台支付交易号)
    private String outTradeNo;

    /// 收钱吧流水号(sn)
    private String outOrderNo;

    /// 订单状态(order_status 原始码, PAID 为支付成功)
    private String tradeStatus;

    /// 通知状态(status, SUCCESS 表示本次通知有效)
    private String notifyStatus;

    /// 订单金额(单位: 分, total_amount)
    private Long amount;

    /// 完成时间(finish_time)
    private OffsetDateTime finishTime;

    /// 买家标识(payer_uid)
    private String buyerId;

    /// 通道支付凭证号(trade_no)
    private String outTransNo;

    /// 原始回调报文(用于对账/排障)
    private String syncData;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;
}
