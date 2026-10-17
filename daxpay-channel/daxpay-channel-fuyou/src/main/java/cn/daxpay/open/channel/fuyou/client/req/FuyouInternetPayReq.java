package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouInternetPayMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 富友互联网直连支付请求(主应用侧)
@Data
@Schema(title = "富友互联网直连支付请求")
public class FuyouInternetPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private FuyouRsaSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为富友 order_id, 回调原样返回凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 商品描述
    private String description;

    /// 支付方式
    @NotNull(message = "{validation.field.method.notNull}")
    private FuyouInternetPayMethod method;

    /// 用户标识(微信 openid / 支付宝 userId, JS 类支付必填)
    private String openId;

    /// 微信 AppId(JSAPI 场景透传 appid, 可选)
    private String wxAppId;

    /// 异步通知地址(由子应用透传给富友 back_notify_url)
    private String notifyUrl;
}
