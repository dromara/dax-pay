package cn.daxpay.open.channel.llianpay.client.req;

import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import cn.daxpay.open.channel.llianpay.client.enums.LlianpayPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 连连通道支付请求(主应用侧)
///
/// 由主应用组装后经声明式 HTTP 客户端转发到子应用, 走连连统收单/收银台下单。
@Data
public class LlianpayPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private LlianpaySdkCredential credential;

    /// 连连支付方式(主应用按平台支付方式白名单映射后传入)
    @NotNull(message = "{validation.field.payMethod.notNull}")
    private LlianpayPayMethod payMethod;

    /// 商户订单号(主应用支付交易号, 作为 txn_seqno 上送)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分, 连连接口单位为元, 由子应用换算后上送)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 用户标识(JSAPI 族必填: 微信 openid / 支付宝买家用户ID)
    private String openId;

    /// 通道应用 AppId(微信 JSAPI/小程序上送 wx_data.appid)
    private String channelAppId;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址
    private String notifyUrl;

    /// 同步跳转地址(仅收银台形态使用)
    private String returnUrl;
}
