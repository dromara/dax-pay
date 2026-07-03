package cn.daxpay.open.channel.alipay.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 支付宝通道退款同步响应
///
/// 与子应用 dax-pay-channel-one 的 `AlipayRefundSyncResp` 镜像, 字段对齐。
/// 子应用原样回传支付宝 trade.fastpay.refund.query 字段, refund_status 映射由主应用完成。
@Data
public class AlipayRefundSyncResp {

    /// 退款状态(REFUND_SUCCESS=退款成功, 空=未查询到/处理中)
    private String refundStatus;

    /// 网关返回码(code, 10000=成功)
    private String code;

    /// 业务返回码(sub_code)
    private String subCode;

    /// 业务返回消息(sub_msg)
    private String subMsg;

    /// 商户订单号(out_trade_no)
    private String outTradeNo;

    /// 支付宝交易号(trade_no)
    private String tradeNo;

    /// 退款请求号(out_request_no)
    private String outRequestNo;

    /// 退款完成时间(gmt_refund_pay)
    private OffsetDateTime finishTime;

    /// 退款金额(refund_amount, 单位: 分)
    private Long refundAmount;
}
