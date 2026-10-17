package cn.daxpay.open.channel.cpcn.client.req;

import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 中金通道订单同步请求(主应用侧)
///
/// 走中金 5016 交易查询, 按商户流水号 TxSN(即下单时上送的平台支付交易号)查询。
@Data
public class CpcnSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private CpcnSdkCredential credential;

    /// 商户订单号(下单时上送的 TxSN = 主应用支付交易号)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;
}
