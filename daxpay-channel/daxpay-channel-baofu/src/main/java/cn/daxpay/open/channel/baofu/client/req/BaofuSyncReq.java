package cn.daxpay.open.channel.baofu.client.req;

import cn.daxpay.open.channel.baofu.client.credential.BaofuSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 宝付通道订单同步请求(主应用侧)
///
/// 走 order_query 按商户订单号查询(无需通道订单号)。
@Data
public class BaofuSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private BaofuSdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;
}
