package cn.daxpay.open.channel.douyin.client.req;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.enums.DouyinPayMethod;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 抖音通道支付请求
///
/// 与子应用 dax-pay-channel-one 的 `DouyinPayReq` 镜像, 经声明式 HTTP 客户端序列化传输, 字段对齐。
@Data
public class DouyinPayReq {
    /// 商户订单号(主应用支付交易号, 作为抖音 out_trade_no)
    private String outTradeNo;
    /// 订单金额(单位: 分)
    private Long amount;
    /// 商品描述
    private String description;
    /// 支付方式
    private DouyinPayMethod method;
    /// 买家标识(JSAPI 支付必填)
    private String openId;
    /// 客户端 IP
    private String clientIp;
    /// 订单过期时间
    private OffsetDateTime expiredTime;
    /// 异步通知地址
    private String notifyUrl;
    /// 是否分账订单(透传抖音分账标识)
    private Boolean allocation;
    /// 通道调用凭证
    private DouyinSdkCredential credential;
}
