package cn.daxpay.open.channel.youzan.client.req;

import cn.daxpay.open.channel.youzan.client.credential.YouzanSdkCredential;
import cn.daxpay.open.channel.youzan.client.enums.YouzanPayBodyType;
import cn.daxpay.open.channel.youzan.client.enums.YouzanPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 有赞通道支付请求
///
/// 由主应用 dax-pay-open 经声明式 HTTP 客户端转发, 走有赞开放网关统一下单
/// (youzan.pay.unified.payopen.apply/1.0.0)。
@Data
public class YouzanPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private YouzanSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 outer_trade_no 上送, 异步回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分, 有赞接口 pay_amount 单位同为分, 直接透传)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题(trade_desc)
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 客户端IP(user_ip)
    private String clientIp;

    /// 异步通知地址(pay_notify_url)
    private String notifyUrl;

    /// 支付工具(pay_tool: ALIPAY_JS / WX_JS / WX_APPLET / WX_H5)
    @NotNull(message = "{validation.field.payMethod.notNull}")
    private YouzanPayMethod method;

    /// 支付内容类型(主应用按支付方式预设, 子应用按此决定 payBody 提取字段)
    private YouzanPayBodyType payBodyType;

    /// 用户标识(微信 open_id / 支付宝 buyer_id, JS/小程序类支付必传)
    private String openId;

    /// 微信 AppId(JS/小程序类支付上送 ext.app_id)
    private String wxAppId;

    /// 支付完成返回地址(ext.return_url, 微信 JS/H5 场景透传)
    private String returnUrl;
}
