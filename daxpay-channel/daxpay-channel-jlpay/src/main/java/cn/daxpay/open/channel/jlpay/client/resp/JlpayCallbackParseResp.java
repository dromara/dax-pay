package cn.daxpay.open.channel.jlpay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 嘉联通道回调验签解析响应
///
/// 子应用对嘉联异步通知(JSON body + 签名请求头)验签解析后回传给主应用。
/// 嘉联仅有支付异步通知(退款为同步终态, 无退款通知),
/// [#ackBody] 为按嘉联规约生成的应答 JSON(`{"ret_code":"00000"}`), 主应用原样返回给嘉联。
@Data
@Accessors(chain = true)
public class JlpayCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(恒 PAY, 嘉联仅支付通知)
    private String tradeType;

    /// 商户侧单号(支付回调 = 原支付交易号 out_trade_no)
    private String outTradeNo;

    /// 嘉联交易号(transaction_id)
    private String outOrderNo;

    /// 支付状态(status 原始码: 2 成功)
    private String tradeStatus;

    /// 订单金额(单位: 分, total_fee)
    private Long amount;

    /// 完成时间(pay_time)
    private OffsetDateTime finishTime;

    /// 买家标识(sub_openid)
    private String buyerId;

    /// 渠道方交易单号(chn_transaction_id)
    private String outTransNo;

    /// 嘉联应答报文(JSON 字符串, 主应用原样回给嘉联)
    private String ackBody;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调报文(用于对账/排障)
    private String syncData;
}
