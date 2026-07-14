package cn.daxpay.open.channel.alipay.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 支付宝通道退款响应
///
/// 与子应用 dax-pay-channel-one 的 `AlipayRefundResp` 镜像, 字段对齐。
/// `complete=false` 表示资金未变动(fund_change=N), 需退款同步查询确认最终状态;
/// `complete=true` 表示资金已变动(fund_change=Y), 退款即时成功。
@Data
public class AlipayRefundResp {

    /// 商户订单号(透传 AlipayRefundReq.outTradeNo)
    private String outTradeNo;

    /// 支付宝交易号(trade_no)
    private String tradeNo;

    /// 退款请求号(透传 AlipayRefundReq.outRequestNo)
    private String outRequestNo;

    /// 资金变动标志(fund_change, Y=已变动 N=未变动)
    private String fundChange;

    /// 是否已终态完成(true 表示 fund_change=Y 退款即时成功)
    private Boolean complete;

    /// 退款完成时间(gmt_refund_pay)
    private OffsetDateTime finishTime;

    /// 退款金额(单位: 分)
    private Long refundAmount;

    /// 买家支付宝用户ID(buyer_user_id)
    private String buyerUserId;

    /// 买家支付宝开放ID(buyer_open_id)
    private String buyerOpenId;
}
