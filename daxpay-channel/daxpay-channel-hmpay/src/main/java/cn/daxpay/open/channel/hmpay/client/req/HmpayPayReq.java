package cn.daxpay.open.channel.hmpay.client.req;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.client.enums.HmpayPayMethod;
import lombok.Data;

/// # 河马付通道支付请求(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `HmpayPayReq` 镜像, 经声明式 HTTP 客户端序列化传输, 字段对齐。
@Data
public class HmpayPayReq {

    /// 通道调用凭证
    private HmpaySdkCredential credential;

    /// 商户订单号(平台支付交易号 tradeNo)
    private String outTradeNo;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 商品标题
    private String title;

    /// 商品描述
    private String description;

    /// 支付方式
    private HmpayPayMethod method;

    /// 用户标识(JSAPI/MINI: 微信 openid / 支付宝 buyerId)
    private String openId;

    /// 第三方应用ID(微信 sub_appid / 支付宝应用ID)
    private String channelAppId;

    /// 付款码(条码支付 B扫C: auth_code)
    private String authCode;

    /// 客户端IP
    private String clientIp;

    /// 订单过期时间
    private String expireTime;

    /// 是否分账(延迟结算)
    private Boolean allocation;

    /// 限制支付方式(如 NO_CREDIT)
    private String limitPay;
}
