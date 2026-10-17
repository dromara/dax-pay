package cn.daxpay.open.channel.shouqianba.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 收钱吧通道退款响应(主应用侧)
///
/// 退款为同步终态: REFUND_SUCCESS 即退款成功(complete=true), 无退款异步通知与退款查询接口。
@Data
@Accessors(chain = true)
public class ShouqianbaRefundResp {

    /// 商户退款单号(透传)
    private String outRefundNo;

    /// 是否已终态完成(REFUND_SUCCESS 时 true)
    private Boolean complete;

    /// 退款金额(单位: 分, 通道返回 total_amount)
    private Long refundAmount;

    /// 原始响应(用于对账/排障)
    private String syncData;
}
