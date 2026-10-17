package cn.daxpay.open.channel.sand.client.req;

import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 杉德通道订单同步请求(主应用 → 子应用)
///
/// 按商户订单号(outOrderNo)查询, 无需通道流水号。
@Data
public class SandSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private SandSdkCredential credential;

    /// 商户订单号(主应用支付交易号 = 下单时的 outOrderNo)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;
}
