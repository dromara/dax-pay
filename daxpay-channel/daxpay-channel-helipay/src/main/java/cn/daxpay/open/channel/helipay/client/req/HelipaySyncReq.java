package cn.daxpay.open.channel.helipay.client.req;

import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 合利宝通道订单同步请求(主应用 → 子应用)
///
/// 走 AppPayQuery 按商户订单号(P2_orderId)查询。
@Data
public class HelipaySyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private HelipaySdkCredential credential;

    /// 商户订单号(原支付交易号)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;
}
