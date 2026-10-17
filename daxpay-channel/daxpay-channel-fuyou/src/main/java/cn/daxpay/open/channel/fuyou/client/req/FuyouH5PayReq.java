package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouH5PayMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 富友 H5 聚合收银台支付请求(主应用侧)
@Data
@Schema(title = "富友 H5 聚合收银台支付请求")
public class FuyouH5PayReq {

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
    private FuyouH5PayMethod method;

    /// 异步通知地址(由子应用透传给富友 back_notify_url)
    private String notifyUrl;

    /// 同步跳转地址(由子应用透传给富友 page_notify_url, 支付完成浏览器跳转)
    private String returnUrl;
}
