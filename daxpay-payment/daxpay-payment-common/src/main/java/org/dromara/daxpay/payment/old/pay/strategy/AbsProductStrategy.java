package org.dromara.daxpay.payment.old.pay.strategy;

import org.dromara.daxpay.payment.common.strategy.PaymentStrategy;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelApiCallMode;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelPayIdType;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayCapabilityEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;

import java.util.List;
import java.util.Map;

/// # 产品策略抽象类
///
public abstract class AbsProductStrategy implements PaymentStrategy {

    /// 是否支持服务商模式
    public boolean isIsv() { return false; }

    /// 是否支持分账
    public boolean isAllocatable() { return false; }

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

    /// 基础模式通道路由可用的支付渠道列表（各产品策略必须显式声明）
    public abstract List<PayProviderEnum> supportedPayProviders();
}
