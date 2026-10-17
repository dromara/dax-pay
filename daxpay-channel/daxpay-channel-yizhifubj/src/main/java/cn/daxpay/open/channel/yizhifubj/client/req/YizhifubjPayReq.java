package cn.daxpay.open.channel.yizhifubj.client.req;

import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import cn.daxpay.open.channel.yizhifubj.client.enums.YizhifubjPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 一直付通道支付请求(主应用侧)
///
/// 由主应用组装后经声明式 HTTP 客户端转发到子应用, 走 5upay 网关 `/onlinePay/order` 统一下单。
@Data
public class YizhifubjPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private YizhifubjSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 requestId 上送, 异步回调凭此反查)
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

    /// 异步通知地址(notifyUrl)
    private String notifyUrl;

    /// 同步跳转地址(callbackUrl)
    private String returnUrl;

    /// 支付方式(决定 paymentModeCode)
    @NotNull(message = "{validation.field.payMethod.notNull}")
    private YizhifubjPayMethod method;

    /// 用户标识(支付宝 JsApi = 买家 userId / 微信 JsApi = openid)
    private String openId;

    /// 微信 appId(微信公众号 JsApi 必填)
    private String appId;
}
