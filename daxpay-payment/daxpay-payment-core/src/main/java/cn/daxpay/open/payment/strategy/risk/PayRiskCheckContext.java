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

    /// 是否启用省级地区拦截（null/false=不检查；true=按 IP 省份匹配省级黑名单）
    ///
    /// 地域策略开关快照, 由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayRiskAssistService]
    /// 读取平台配置后注入。
    private Boolean provinceBlacklistEnabled;

    /// 是否启用地理围栏（null/false=不检查；true=启用门店市级围栏检测）
    ///
    /// 第三层围栏全局开关快照, 由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayRiskAssistService]
    /// 读取平台配置后注入。开启后比对 IP 归属城市与门店所在城市, 不一致按 blockOnHit 处理。
    private Boolean geoFenceEnabled;

    /// 门店号（围栏命中快照来源）
    ///
    /// 由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayRiskAssistService]
    /// 从 terminal.storeNo 提取注入。
    private String storeNo;

    /// 门店所在城市（围栏比对基准, 中文城市名）
    ///
    /// 由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayRiskAssistService]
    /// 按 storeNo 查门店 regionCode 反查 base_city 名称注入; 门店无地址时为 null（围栏 fail-open）。
    private String storeCity;

    /// 客户端 IP 归属城市（命中落库快照, 由检查器解析 IP 后填入）
    private String clientCity;
}
