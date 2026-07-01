package cn.daxpay.open.channel.alipay.client.req;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.enums.AlipayPayMethod;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 支付宝通道支付请求
///
/// 与子应用 dax-pay-channel-one 的 `AlipayPayReq` 镜像, 经声明式 HTTP 客户端 [cn.daxpay.open.channel.alipay.client.AlipayChannelClient] 序列化传输, 字段对齐。
@Data
public class AlipayPayReq {
    /// 商户订单号(主应用支付交易号, 作为支付宝 out_trade_no)
    private String outTradeNo;
    /// 订单金额(单位: 分)
    private Long amount;
    /// 订单标题
    private String subject;
    /// 订单描述(对应支付宝 body)
    private String body;
    /// 支付方式
    private AlipayPayMethod method;
    /// 订单过期时间
    private OffsetDateTime expireTime;
    /// 异步通知地址
    private String notifyUrl;
    /// 付款码(BARCODE 付款码支付必填)
    private String authCode;
    /// 买家标识(JSAPI 小程序支付必填; 2088 开头为支付宝用户ID, 否则视为小程序 openid)
    private String openId;
    /// 通道调用凭证
    private AlipaySdkCredential credential;
}
