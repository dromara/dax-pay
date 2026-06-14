package org.dromara.daxpay.channel.alipay.strategy;

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

/// # 支付宝直连产品策略
///
/// 支付宝直连模式的支付产品策略，定义支持条码、扫码、JSAPI、小程序、PC、H5和APP等全部支付方式，使用商户API调用模式并支持沙箱环境。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_ORDER_QR)),
            Map.entry(PayMethodEnum.ALIPAY_ORDER_QR, List.of(PayCapabilityEnum.ALIPAY_ORDER_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_MINI, List.of(PayCapabilityEnum.ALIPAY_MINI)),
            Map.entry(PayMethodEnum.ALIPAY_PC, List.of(PayCapabilityEnum.ALIPAY_PC)),
            Map.entry(PayMethodEnum.ALIPAY_H5, List.of(PayCapabilityEnum.ALIPAY_H5)),
            Map.entry(PayMethodEnum.ALIPAY_APP, List.of(PayCapabilityEnum.ALIPAY_APP)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY;
    }

    @Override
    public boolean isAllocatable() {
        return true;
    }

    @Override
    public boolean isApply() {
        return true;
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
        return List.of(ALIPAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
