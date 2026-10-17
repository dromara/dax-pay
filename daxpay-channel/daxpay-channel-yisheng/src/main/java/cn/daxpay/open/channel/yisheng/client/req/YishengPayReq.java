package cn.daxpay.open.channel.yisheng.client.req;

import cn.daxpay.open.channel.yisheng.client.credential.YishengSdkCredential;
import cn.daxpay.open.channel.yisheng.client.enums.YishengPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 易生通道支付请求
///
/// 由主应用 dax-pay-open 经声明式 HTTP 客户端转发。
/// 支付方式由 method(接口) + payType(底层渠道与形态) 组合决定:
/// - QRCODE: 走 easypay.qrcode.pay.push, 返回 code_url 二维码
/// - JSAPI: 走 easypay.js.pay.push, 需 openId, 返回 pay_info 调起参数
@Data
public class YishengPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private YishengSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 out_trade_no 上送, 异步回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分, 易生接口金额单位同为分, 直接透传)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付方式(QRCODE 扫码 / JSAPI)
    @NotNull(message = "{validation.field.method.notNull}")
    private YishengPayMethod method;

    /// 易生支付类型(aliPay / wxNative / aliJsPay / wxJsPay)
    @NotBlank(message = "{validation.field.payType.notBlank}")
    private String payType;

    /// 用户标识(JSAPI 必填: 微信 openid / 支付宝买家 uid)
    private String openId;

    /// 异步通知地址(由子应用透传给易生)
    private String notifyUrl;
}
