package cn.daxpay.open.channel.duolabao.client.req;

import cn.daxpay.open.channel.duolabao.client.credential.DuolabaoSdkCredential;
import cn.daxpay.open.channel.duolabao.client.enums.DuolabaoPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 哆啦宝通道支付请求(主应用侧)
///
/// 由主应用组装后经声明式 HTTP 客户端转发到子应用。
@Data
public class DuolabaoPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private DuolabaoSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 requestNum 上送, 异步回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分, 哆啦宝接口为元, 由子应用换算上送)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付方式(决定接口形态与渠道参数)
    @NotNull(message = "{validation.field.method.notNull}")
    private DuolabaoPayMethod payMethod;

    /// 用户标识(JS 支付必填: 支付宝买家用户ID / 微信 openid)
    private String openId;

    /// 通道应用 AppId(微信 JS 支付上送 appId/subAppId, 可选)
    private String channelAppId;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址(callbackUrl)
    private String notifyUrl;

    /// 同步跳转地址(completeUrl, 聚合码支付完成后跳转, 可选)
    private String returnUrl;

    /// 订单过期时间(空则按默认有效期兜底)
    private OffsetDateTime expireTime;
}
