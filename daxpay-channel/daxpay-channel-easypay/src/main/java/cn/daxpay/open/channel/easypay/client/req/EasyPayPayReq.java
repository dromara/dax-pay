package cn.daxpay.open.channel.easypay.client.req;

import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import cn.daxpay.open.channel.easypay.client.enums.EasyPayPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 易支付通道支付请求
///
/// 由主应用 dax-pay-open 经声明式 HTTP 客户端转发。
/// 支付方式由 payMethod 决定易支付 `method/device/type` 三参数组合(见 [EasyPayPayMethod]),
/// 一期仅扫码两类(ALIPAY_QR/WECHAT_QR)。
///
/// 金额单位为「分」(平台内部单位), 子应用换算为易支付「元」(两位小数字符串)后上送。
@Data
public class EasyPayPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private EasyPaySdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为易支付 out_trade_no, 异步回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品描述(作为易支付 name 字段)
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付方式(决定 method/device/type 参数组合)
    @NotNull(message = "{validation.field.method.notNull}")
    private EasyPayPayMethod payMethod;

    /// 客户端IP(作为易支付 clientip 字段, 可空)
    private String clientIp;

    /// 异步通知地址(由子应用透传给易支付平台)
    private String notifyUrl;

    /// 支付完跳转地址(由子应用透传给易支付平台, 可空)
    private String returnUrl;
}
