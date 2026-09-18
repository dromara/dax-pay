package cn.daxpay.open.channel.easypay.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 易支付通道退款响应
///
/// 子应用调用易支付退款 API 后回传给主应用。
/// 易支付退款为同步受理即成功模式(移植自商业版, 无退款异步通知), `complete` 恒为 true。
@Data
public class EasyPayRefundResp {

    /// 商户退款单号(透传 EasyPayRefundReq.outRefundNo)
    private String outRefundNo;

    /// 易支付退款单号(refund_no)
    private String tradeNo;

    /// 是否已终态成功(同步受理即成功, 恒为 true)
    private Boolean complete;
}
