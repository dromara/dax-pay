package cn.daxpay.open.channel.umfpay.client.req;

import cn.daxpay.open.channel.umfpay.client.credential.UmfpaySdkCredential;
import cn.daxpay.open.channel.umfpay.client.enums.UmfpayPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 联动优势通道支付请求(主应用侧)
///
/// 由主应用组装后经声明式 HTTP 客户端转发到子应用, 走老网关扫码/公众号下单。
@Data
public class UmfpayPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private UmfpaySdkCredential credential;

    /// 联动优势支付方式(主应用按平台支付方式白名单映射后传入)
    @NotNull(message = "{validation.field.payMethod.notNull}")
    private UmfpayPayMethod payMethod;

    /// 商户订单号(主应用支付交易号, 作为 order_id 上送)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分, 联动优势接口单位同为分, 直接透传)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题(goods_inf)
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 客户端IP(user_ip, 同时派生 consumer_id)
    private String clientIp;

    /// 异步通知地址(notify_url)
    private String notifyUrl;

    /// 同步跳转地址(ret_url, 仅公众号支付使用)
    private String returnUrl;
}
