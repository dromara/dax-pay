package org.dromara.daxpay.channel.lakala.strategy;

import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelApiCallMode;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelPayIdType;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayCapabilityEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.payment.pay.strategy.AbsProductStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum.ALIPAY;
import static org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum.UNION_PAY;
import static org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum.WECHAT;

/// # 拉卡拉支付产品策略
///
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.WECHAT_BARCODE, List.of(PayCapabilityEnum.WECHAT_BARCODE)),
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            Map.entry(PayMethodEnum.UNION_PAY_BARCODE, List.of(PayCapabilityEnum.UNION_PAY_BARCODE)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_APP, List.of(PayCapabilityEnum.WECHAT_APP)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_ORDER_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_MINI, List.of(PayCapabilityEnum.ALIPAY_MINI)),
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_PAY_QR)),
            Map.entry(PayMethodEnum.UNION_JSAPI, List.of(PayCapabilityEnum.UNION_PAY_JSAPI)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_PAY;
    }

    @Override
    public boolean isIsv() { return true; }

    @Override
    public boolean isAllocatable() { return true; }

    @Override
    public boolean isTerminal() { return true; }

    @Override
    public boolean isApply() { return true; }

    @Override
    public boolean isSandbox() { return true; }

    @Override
    public ChannelApiCallMode getApiCallMode() { return ChannelApiCallMode.ISV; }

    @Override
    public ChannelPayIdType getPayIdType() { return ChannelPayIdType.MCH; }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(WECHAT, ALIPAY, UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
