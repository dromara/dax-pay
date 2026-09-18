package cn.daxpay.open.channel.easypay.client.req;

import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 易支付通道关单请求
///
/// 由主应用 dax-pay-open 经声明式 HTTP 客户端转发。
/// 通道交易号(originTradeNo)优先, 为空时用商户订单号(originOutTradeNo)。
@Data
public class EasyPayCloseReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private EasyPaySdkCredential credential;

    /// 原商户订单号(主应用支付交易号)
    private String originOutTradeNo;

    /// 原易支付交易号(优先使用)
    private String originTradeNo;
}
