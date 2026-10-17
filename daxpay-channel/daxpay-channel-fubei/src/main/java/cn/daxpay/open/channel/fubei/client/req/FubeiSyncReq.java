package cn.daxpay.open.channel.fubei.client.req;

import cn.daxpay.open.channel.fubei.client.credential.FubeiSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 付呗通道订单同步请求(主应用侧)
///
/// 走 fbpay.order.query, 按商户订单号(merchant_order_sn = 平台支付交易号)查询。
@Data
public class FubeiSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private FubeiSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 即下单时的 merchant_order_sn)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;
}
