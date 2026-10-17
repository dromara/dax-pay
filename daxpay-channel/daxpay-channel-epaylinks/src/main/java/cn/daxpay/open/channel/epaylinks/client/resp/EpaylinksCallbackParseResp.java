package cn.daxpay.open.channel.epaylinks.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 易票联通道回调验签解析响应(主应用侧)
@Data
@Accessors(chain = true)
public class EpaylinksCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(恒 PAY 支付回调)
    private String tradeType;

    /// 商户侧单号(outTradeNo = 平台支付交易号)
    private String outTradeNo;

    /// 易票联平台交易号(transactionNo)
    private String outOrderNo;

    /// 交易状态(payState 原始码)
    private String tradeStatus;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 完成时间(payTime, 东八区)
    private OffsetDateTime finishTime;

    /// 买家标识(buyerId / openId)
    private String buyerId;

    /// 渠道单号(channelOrder)
    private String outTransNo;

    /// 应答报文(JSON, 主应用原样回给易票联)
    private String ackBody;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调明文(用于对账/排障)
    private String syncData;
}
