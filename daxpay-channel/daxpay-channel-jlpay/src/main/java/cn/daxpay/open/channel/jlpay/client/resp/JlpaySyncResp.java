package cn.daxpay.open.channel.jlpay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 嘉联通道订单同步响应
///
/// 子应用调用嘉联 /open/trans/chnquery 查单后回传给主应用。
/// tradeState 为嘉联原始支付状态(status: 2 成功 / 其他未成功),
/// tranSts 为交易处理状态(1 处理中, 非 1 为失败终态), 二者一并回传由主应用映射。
@Data
@Accessors(chain = true)
public class JlpaySyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 嘉联交易号(transaction_id)
    private String outOrderNo;

    /// 嘉联支付状态(status 原始码: 2 成功)
    private String tradeState;

    /// 嘉联交易处理状态(tranSts 原始码: 1 处理中, 非 1 失败终态)
    private String tranSts;

    /// 用户实付金额(单位: 分, 成功时返回 total_fee)
    private Long realAmount;

    /// 完成时间(成功时返回 pay_time)
    private OffsetDateTime finishTime;

    /// 买家标识(成功时返回 sub_openid)
    private String buyerId;

    /// 渠道方交易单号(成功时返回 chn_transaction_id)
    private String outTransNo;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
