package cn.daxpay.open.payment.strategy.product;

import cn.daxpay.open.payment.strategy.PaymentStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelApiCallMode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelPayIdType;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;

import java.util.List;
import java.util.Map;
import java.util.Set;

/// # 产品策略抽象类
///
public abstract class AbsProductStrategy implements PaymentStrategy {

    /// 是否支持服务商模式
    public boolean isIsv() { return false; }

    /// 是否支持终端报备
    public boolean isTerminal() { return false; }

    /// 是否支持沙箱环境
    public boolean isSandbox() { return false; }

    /// API调用模式
    public ChannelApiCallMode getApiCallMode() { return ChannelApiCallMode.MCH; }

    /// 支付标识类型
    public ChannelPayIdType getPayIdType() { return ChannelPayIdType.MCH; }

    /// 目录支付方式 → 本产品在该方式下可用的支付能力列表（发版契约，对齐 PayMethodEnum / PayCapabilityEnum）
    public abstract Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping();

    /// 需要绑定微信应用(解析 wxAppId)的支付能力集
    ///
    /// 返回 null 表示该产品所有微信支付能力均可绑定应用(不限制);
    /// 聚合类产品(如 Adapay/乐刷)仅部分微信能力需要 appid, 覆写返回收窄子集,
    /// 弹窗候选([WxPlatformAppCapabilityService#listSupportedCapabilities])与
    /// 支付链路(通道 Assembler/PayService)统一读取此声明。
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() { return null; }

    /// 基础模式通道路由可用的支付渠道列表（各产品策略必须显式声明）
    public abstract List<PayProviderEnum> supportedPayProviders();
}
