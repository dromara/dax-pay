package cn.daxpay.open.channel.jlpay.client.req;

import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import cn.daxpay.open.channel.jlpay.client.enums.JlpayPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 嘉联通道支付请求
///
/// 由主应用 dax-pay-open 经声明式 HTTP 客户端转发:
/// - QRCODE(主扫): 走 /open/trans/qrcodepay, payType 必填(wxpay/alipay/unionpay)
/// - MICROPAY(被扫): 走 /open/trans/micropay, authCode 必填(嘉联自动识别渠道)
@Data
public class JlpayPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private JlpaySdkCredential credential;

    /// 嘉联支付方式(主扫/被扫)
    @NotNull(message = "{validation.field.payMethod.notNull}")
    private JlpayPayMethod method;

    /// 商户订单号(主应用支付交易号, 上送 out_trade_no, 异步回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分, 嘉联接口金额单位同为分, 直接透传)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题(上送 body)
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 商品描述(上送 attach, 可空)
    private String description;

    /// 渠道类型(主扫必填: wxpay/alipay/unionpay; 被扫不传, 凭 auth_code 识别)
    private String payType;

    /// 付款码(被扫必填)
    private String authCode;

    /// 客户端IP(上送 mch_create_ip)
    private String clientIp;

    /// 异步通知地址(notify_url, 由子应用透传给嘉联)
    private String notifyUrl;
}
