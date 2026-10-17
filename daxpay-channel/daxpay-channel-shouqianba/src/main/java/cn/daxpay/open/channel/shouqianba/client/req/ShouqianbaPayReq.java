package cn.daxpay.open.channel.shouqianba.client.req;

import cn.daxpay.open.channel.shouqianba.client.credential.ShouqianbaSdkCredential;
import cn.daxpay.open.channel.shouqianba.client.enums.ShouqianbaPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 收钱吧通道支付请求(主应用侧)
///
/// 由主应用组装后经声明式 HTTP 客户端转发到子应用,
/// 扫码类走 upay/v2/precreate 预下单, 微信 WAP 走 wap api pro 网关跳转。
@Data
public class ShouqianbaPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private ShouqianbaSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 client_sn 上送, 异步回调凭此反查; 收钱吧限制 5-32 位)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题(收钱吧必填, 建议中文)
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付方式(决定 payway 与下单形态)
    @NotNull(message = "{validation.field.payMethod.notNull}")
    private ShouqianbaPayMethod method;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址(收钱吧回调入口)
    private String notifyUrl;

    /// 支付完成前端跳转地址(仅 WAP 收银台形态使用)
    private String returnUrl;
}
