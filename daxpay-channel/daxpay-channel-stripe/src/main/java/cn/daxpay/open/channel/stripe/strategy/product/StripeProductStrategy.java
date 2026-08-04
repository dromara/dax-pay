package cn.daxpay.open.channel.stripe.strategy.product;

import cn.daxpay.open.platform.core.enums.pay.channel.ChannelApiCallMode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelPayIdType;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/// # Stripe 产品策略
///
/// 单一产品 STRIPE_PAY 覆盖 Visa / MasterCard 卡组网关收单。
/// 支付方式→支付能力映射对齐 PayMethodEnum / PayCapabilityEnum 的卡组预留项。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.of(
            PayMethodEnum.VISA_CARD_GATEWAY, List.of(PayCapabilityEnum.VISA_CARD_GATEWAY),
            PayMethodEnum.MASTERCARD_CARD_GATEWAY, List.of(PayCapabilityEnum.MASTERCARD_CARD_GATEWAY));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.STRIPE_PAY;
    }

    @Override
    public boolean isSandbox() {
        return true;
    }

    @Override
    public ChannelApiCallMode getApiCallMode() {
        return ChannelApiCallMode.MCH;
    }

    @Override
    public ChannelPayIdType getPayIdType() {
        return ChannelPayIdType.MCH;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.VISA, PayProviderEnum.MASTERCARD);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
