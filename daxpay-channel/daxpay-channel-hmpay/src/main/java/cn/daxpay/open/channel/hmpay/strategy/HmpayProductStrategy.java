package cn.daxpay.open.channel.hmpay.strategy;

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

/// # 河马付支付产品策略
///
/// 声明河马付(杉德)为聚合服务商模式(ISV), 一个产品覆盖微信/支付宝两类底层渠道,
/// 同时支持聚合扫码(自动识别)与条码(付款码)。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射(河马付支持的支付方式)
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 聚合(扫码/条码, 自动识别底层渠道)
            Map.entry(PayMethodEnum.AGGREGATE_PAY_QRCODE, List.of(PayCapabilityEnum.AGGREGATE_PAY_QRCODE)),
            Map.entry(PayMethodEnum.AGGREGATE_PAY_BARCODE, List.of(PayCapabilityEnum.AGGREGATE_PAY_BARCODE)),
            // 微信(扫码/JSAPI/小程序)
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            // 支付宝(扫码/JSAPI/小程序)
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_MINI, List.of(PayCapabilityEnum.ALIPAY_MINI)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HM_PAY;
    }

    @Override
    public boolean isIsv() {
        return true;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isSandbox() {
        return true;
    }

    @Override
    public ChannelApiCallMode getApiCallMode() {
        return ChannelApiCallMode.ISV;
    }

    @Override
    public ChannelPayIdType getPayIdType() {
        return ChannelPayIdType.MCH;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
