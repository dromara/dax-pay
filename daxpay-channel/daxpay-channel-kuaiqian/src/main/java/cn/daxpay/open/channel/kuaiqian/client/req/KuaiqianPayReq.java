package cn.daxpay.open.channel.kuaiqian.client.req;

import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 快钱通道支付请求(主应用侧)
///
/// 由主应用组装后经声明式 HTTP 客户端转发到子应用, 走 MAS 网关 A7007 当面付聚合扫码下单。
@Data
public class KuaiqianPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private KuaiqianSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 externalRefNumber 上送, 异步回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址(tr3Url)
    private String notifyUrl;
}
