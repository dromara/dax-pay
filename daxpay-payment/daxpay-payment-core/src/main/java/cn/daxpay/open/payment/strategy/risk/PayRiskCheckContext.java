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

    /// 事前命中是否阻断下单（null/true=阻断；false=仅落命中记录）
    private Boolean blockOnHit;

    /// 是否启用黑名单拦截（null/false=不检查；true=执行 IP/用户标识黑名单检查）
    ///
    /// 第一层黑名单开关快照, 由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayRiskAssistService]
    /// 读取平台配置后注入。
    private Boolean blacklistEnabled;

    /// 是否拦截海外 IP（null/false=不拦截；true=拦截）
    ///
    /// 地域策略开关快照, 由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayRiskAssistService]
    /// 读取平台配置后注入, 供检查器按 IP 归属地判定是否命中。
    private Boolean blockOverseasIp;

    /// 是否启用地区拦截（null/false=不检查；true=按 IP 归属地匹配省级与市级黑名单）
    ///
    /// 地域策略开关快照, 由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayRiskAssistService]
    /// 读取平台配置后注入。合并原省级/市级独立开关, 省级命中后不再执行市级检查。
    private Boolean regionBlacklistEnabled;

    /// 是否启用 IPv6 地区匹配（null/false=IPv6 直通放行；true=对 IPv6 执行地区匹配）
    ///
    /// 地域策略开关快照, 由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayRiskAssistService]
    /// 读取平台配置后注入。关闭时省级/市级/海外三项检查对 IPv6 直通放行;
    /// 开启时 IPv6 走 ip2region 查询(xdb 离线数据精度有限)。
    private Boolean ipv6MatchEnabled;

    /// 客户端 IP 归属城市（命中落库快照, 由检查器解析 IP 后填入）
    private String clientCity;
}
