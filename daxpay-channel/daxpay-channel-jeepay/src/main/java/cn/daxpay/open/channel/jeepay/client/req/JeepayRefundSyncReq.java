package cn.daxpay.open.channel.jeepay.client.req;

import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # Jeepay通道退款同步请求(主应用侧)
///
/// 通道退款单号(originTradeNo, 即 Jeepay refundOrderId)优先, 为空时用商户退款单号(outRefundNo, 即 mchRefundNo)。
@Data
public class JeepayRefundSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private JeepaySdkCredential credential;

    /// 商户退款单号(主应用退款交易号)
    private String outRefundNo;

    /// Jeepay退款单号(refundOrderId, 优先使用)
    private String originTradeNo;
}
