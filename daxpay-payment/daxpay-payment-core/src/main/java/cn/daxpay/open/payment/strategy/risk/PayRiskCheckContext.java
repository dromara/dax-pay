package cn.daxpay.open.payment.strategy.risk;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付风控检查上下文
///
/// 字段均为快照，供名单比对与命中落库；core 不依赖插件表结构。
@Data
@Accessors(chain = true)
public class PayRiskCheckContext {

    /// 阶段：before_pay / after_pay
    private String phase;

    /// 入口场景：api / gateway / code / manual / unknown
    private String scene;

    /// 商户号
    private String mchNo;

    /// 应用号
    private String appId;

    /// 客户端 IP
    private String clientIp;

    /// 下单 openId
    private String openId;

    /// 通道回写付款人标识（buyerId / openId）
    private String buyerId;

    /// 平台交易号
    private String tradeNo;

    /// 业务容器单号
    private String orderNo;

    /// 商户业务单号
    private String bizOrderNo;

    /// 交易类型（normal / gateway 等）
    private String tradeType;

    /// 支付方式
    private String method;

    /// 支付产品
    private String product;

    /// 支付通道
    private String channel;

    /// 通道应用 AppId（openId 精细匹配可选）
    private String channelAppId;
}
