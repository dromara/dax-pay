package cn.daxpay.open.channel.alipay.strategy.isv.product;

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

/// # 支付宝服务商产品策略
///
/// 支付宝服务商(ISV)模式的支付产品策略，定义支持条码、扫码、JSAPI(含小程序)、PC、H5和APP等全部支付方式，使用服务商API调用模式。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            // 支付宝扫码(底层 alipay.trade.precreate 预下单)
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            // 支付宝 JSAPI(含小程序, 官方 JSAPI_PAY)
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_PC, List.of(PayCapabilityEnum.ALIPAY_PC)),
            Map.entry(PayMethodEnum.ALIPAY_H5, List.of(PayCapabilityEnum.ALIPAY_H5)),
            Map.entry(PayMethodEnum.ALIPAY_APP, List.of(PayCapabilityEnum.ALIPAY_APP)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY_ISV;
    }

    @Override
    public boolean isIsv() { return true; }

    @Override
    public boolean isSandbox() { return false; }

    @Override
    public ChannelApiCallMode getApiCallMode() { return ChannelApiCallMode.ISV; }

    @Override
    public ChannelPayIdType getPayIdType() { return ChannelPayIdType.IDENTITY; }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.ALIPAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
