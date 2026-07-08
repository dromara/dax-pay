package cn.daxpay.open.channel.fuyou.strategy;

import cn.daxpay.open.platform.core.enums.pay.channel.ChannelApiCallMode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelPayIdType;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.core.strategy.product.AbsProductStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum.ALIPAY;
import static cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum.UNION_PAY;
import static cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum.WECHAT;

/// # 富友支付产品策略
///
/// 声明富友聚合通道支持的服务商模式 + 微信/支付宝/银联 的 JSAPI/扫码/付款码。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 微信
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_BARCODE, List.of(PayCapabilityEnum.WECHAT_BARCODE)),
            // 支付宝
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_MINI, List.of(PayCapabilityEnum.ALIPAY_MINI)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            // 银联
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_PAY_QR)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_PAY;
    }

    @Override
    public boolean isIsv() { return true; }

    @Override
    public boolean isTerminal() { return false; }

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
