package cn.daxpay.open.channel.baofu.client.req;

import cn.daxpay.open.channel.baofu.client.credential.BaofuSdkCredential;
import cn.daxpay.open.channel.baofu.client.enums.BaofuPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 宝付通道支付请求(主应用侧)
///
/// 由主应用组装后经声明式 HTTP 客户端转发到子应用, 按支付方式走 unified_order 或 pre_unified_order 下单。
@Data
public class BaofuPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private BaofuSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付方式(决定 payCode 与接口形态)
    @NotNull(message = "{validation.field.method.notNull}")
    private BaofuPayMethod method;

    /// 用户标识(微信 sub_openid / 支付宝 buyer_id / 云闪付 userId, JSAPI 族必填)
    private String openId;

    /// 付款码(微信/支付宝被扫必填)
    private String authCode;

    /// 微信 AppId(公众号/小程序, WECHAT_JSAPI 族 payExtend.sub_appid)
    private String wxAppId;

    /// 客户端IP(riskInfo.clientIp / 云闪付 customerIp)
    private String clientIp;

    /// 异步通知地址(notifyUrl)
    private String notifyUrl;

    /// 前端回跳地址(pageUrl)
    private String returnUrl;
}
