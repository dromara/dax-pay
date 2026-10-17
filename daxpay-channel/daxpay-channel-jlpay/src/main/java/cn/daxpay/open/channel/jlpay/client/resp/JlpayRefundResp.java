package cn.daxpay.open.channel.jlpay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 嘉联通道退款响应
///
/// 子应用调用嘉联 /open/trans/refund 退款后回传给主应用。
/// 嘉联退款为同步终态: 受理成功(ret_code=00)即退款成功(complete=true),
/// 无退款异步通知与退款查询接口。
@Data
@Accessors(chain = true)
public class JlpayRefundResp {

    /// 商户退款单号(透传 JlpayRefundReq.outRefundNo)
    private String outRefundNo;

    /// 嘉联退款交易号(transaction_id)
    private String outRefundOrderNo;

    /// 退款金额(单位: 分, total_fee)
    private Long refundAmount;

    /// 是否已终态完成(嘉联退款同步终态, 受理成功恒 true)
    private Boolean complete;

    /// 完成时间(嘉联退款响应无独立时间字段, 为空)
    private OffsetDateTime finishTime;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
