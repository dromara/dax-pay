package cn.daxpay.open.channel.easypay.client.req;

import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 易支付通道退款同步请求
///
/// 由主应用 dax-pay-open 经声明式 HTTP 客户端转发。
/// 通道退款单号(originTradeNo, 即易支付 refund_no)优先, 为空时用商户退款单号(outRefundNo)。
@Data
public class EasyPayRefundSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private EasyPaySdkCredential credential;

    /// 商户退款单号(主应用退款交易号)
    private String outRefundNo;

    /// 易支付退款单号(refund_no, 优先使用)
    private String originTradeNo;
}
