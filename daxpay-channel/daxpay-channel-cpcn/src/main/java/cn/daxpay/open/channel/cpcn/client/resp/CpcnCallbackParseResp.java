package cn.daxpay.open.channel.cpcn.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 中金通道回调验签解析响应(主应用侧)
///
/// 子应用对中金异步通知(message + signature)验签解析后回传给主应用。
/// 支付回调与退款回调共用本对象, 由 [#tradeType] 区分(PAY/REFUND), 字段按场景取值;
/// [#ackBody] 为按中金规约生成的应答报文(Base64 XML), 主应用原样返回给中金。
@Data
@Accessors(chain = true)
public class CpcnCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户侧单号(支付回调 = 原支付 TxSN; 退款回调 = 退款 TxSN)
    private String outTradeNo;

    /// 中金订单号(OrderNo)
    private String outOrderNo;

    /// 交易状态(中金 Status 原始码: 支付 30 成功 / 退款 20 成功 / 40 失败)
    private String tradeStatus;

    /// 金额(单位: 分)
    private Long amount;

    /// 完成时间(ResponseTime)
    private OffsetDateTime finishTime;

    /// 买家标识(PayerID)
    private String buyerId;

    /// 中金流水号(TraceNo)
    private String outTransNo;

    /// 应答报文(Base64 XML, 主应用原样回给中金)
    private String ackBody;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调明文(Base64 解码后的 JSON, 用于对账/排障)
    private String syncData;
}
