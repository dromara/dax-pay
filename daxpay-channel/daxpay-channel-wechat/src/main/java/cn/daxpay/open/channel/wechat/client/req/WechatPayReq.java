package cn.daxpay.open.channel.wechat.client.req;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.enums.WechatPayMethod;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 微信通道支付请求
///
/// 与子应用 dax-pay-channel-one 的 `WechatPayReq` 镜像, 经声明式 HTTP 客户端
/// [cn.daxpay.open.channel.wechat.client.WechatChannelClient] 序列化传输, 字段对齐。
@Data
public class WechatPayReq {
    /// 商户订单号(主应用支付交易号, 作为微信 out_trade_no)
    private String outTradeNo;
    /// 订单金额(单位: 分)
    private Long amount;
    /// 商品描述(对应微信 description)
    private String description;
    /// 支付方式
    private WechatPayMethod method;
    /// 订单过期时间
    private OffsetDateTime expireTime;
    /// 异步通知地址
    private String notifyUrl;
    /// 附加数据(对应微信 attach)
    private String attach;
    /// 用户标识(JSAPI / MINI 必填)
    private String openId;
    /// 付款码(MICROPAY 必填)
    private String authCode;
    /// 用户终端IP(H5 场景必填)
    private String payerClientIp;
    /// H5 场景 wap_url
    private String wapUrl;
    /// H5 场景 wap_name
    private String wapName;
    /// 通道调用凭证
    private WechatSdkCredential credential;
}
