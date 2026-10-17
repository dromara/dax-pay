package cn.daxpay.open.channel.helipay.client.req;

import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import cn.daxpay.open.channel.helipay.client.enums.HelipayPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 合利宝通道支付请求(主应用 → 子应用)
///
/// 由主应用经声明式 HTTP 客户端转发, 按 [HelipayPayMethod] 走对应业务形态
/// (AppPay 扫码 / AppPayPublic 公众号 / AppPayH5WFT H5)。
///
/// 金额单位: 平台内部为「分」, 合利宝接口为「元」, 子应用负责换算。
@Data
public class HelipayPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private HelipaySdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 P2_orderId 上送, 异步回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分, 子应用换算为元上送)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付方式(决定 bizType/appType 参数组装)
    @NotNull(message = "{validation.field.method.notNull}")
    private HelipayPayMethod payMethod;

    /// 用户标识(公众号 JS 族必填: 微信 openid / 支付宝 buyerId, 其他方式忽略)
    private String openId;

    /// 客户端IP(orderIp)
    private String clientIp;

    /// 异步通知地址(由子应用透传给合利宝)
    private String notifyUrl;

    /// 支付完跳转地址(H5/公众号场景透传 successToUrl, 可空)
    private String returnUrl;
}
