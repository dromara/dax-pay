package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.client.enums.LakalaPayBodyType;
import cn.daxpay.open.channel.lakala.client.enums.MossPayMethod;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 拉卡拉 MOSS 支付请求(主应用侧, 与子应用镜像)
///
/// 支付方式由 method + accountType + transType 三要素决定:
/// - CASHIER(收银台): pay_scene=0, 返回 counter_url
/// - PREORDER(直连下单): pay_scene=1, 配合 accountType + transType
@Data
public class MossPayReq {

    private MossSdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 商品标题
    private String title;

    /// 商品描述
    private String description;

    /// 支付方式(CASHIER 收银台 / PREORDER 直连下单)
    private MossPayMethod method;

    /// 账户类型(WECHAT / ALIPAY / UQRCODEPAY)
    private String accountType;

    /// 交易类型(41扫码 / 51 JSAPI / 71 小程序)
    private String transType;

    /// 用户标识(微信 openid / 支付宝 buyerId)
    private String openId;

    /// 微信 JSAPI sub_appid
    private String channelAppId;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址
    private String notifyUrl;

    /// 同步跳转地址(CASHIER 场景上送 callback_url)
    private String returnUrl;

    /// 订单过期时间(子应用换算 order_eff_time 分钟数)
    private OffsetDateTime expireTime;

    /// 支付内容类型(主应用预先计算好, 子应用透传到响应)
    private LakalaPayBodyType payBodyType;
}
