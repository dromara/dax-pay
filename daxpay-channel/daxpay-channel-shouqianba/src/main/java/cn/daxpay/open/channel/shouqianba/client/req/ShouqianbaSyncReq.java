package cn.daxpay.open.channel.shouqianba.client.req;

import cn.daxpay.open.channel.shouqianba.client.credential.ShouqianbaSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 收钱吧通道订单同步请求(主应用侧)
///
/// 走 upay/v2/query 按 client_sn(商户订单号)查询订单状态。
@Data
public class ShouqianbaSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private ShouqianbaSdkCredential credential;

    /// 商户订单号(支付时上送的 client_sn, 即主平台支付交易号)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;
}
