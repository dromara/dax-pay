package cn.daxpay.open.channel.epaylinks.client.req;

import cn.daxpay.open.channel.epaylinks.client.credential.EpaylinksSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 易票联通道订单同步请求(主应用侧)
@Data
public class EpaylinksSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private EpaylinksSdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;
}
