package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouPayBodyType;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 富友通道支付请求(主应用侧)
@Data
public class FuyouPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private FuyouSdkCredential credential;

    /// 商户订单号(平台支付交易号)
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
    private FuyouPayMethod method;

    /// 用户标识(微信 openid / 支付宝 buyerId)
    private String openId;

    /// 微信 AppId(JSAPI 场景透传)
    private String wxAppId;

    /// 付款码(BARCODE 必填)
    private String authCode;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址
    private String notifyUrl;

    /// 是否限制信用卡支付
    private Boolean limitCredit;

    /// 订单过期时间
    private OffsetDateTime expireTime;

    /// 支付内容类型
    private FuyouPayBodyType payBodyType;
}
