package cn.daxpay.open.channel.jeepay.client.req;

import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # Jeepay通道订单同步请求(主应用侧)
///
/// 通道交易号(tradeNo, 即 Jeepay payOrderId)优先, 为空时用商户订单号(outTradeNo)查询。
@Data
public class JeepaySyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private JeepaySdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;

    /// Jeepay支付订单号(payOrderId, 优先使用)
    private String tradeNo;
}
