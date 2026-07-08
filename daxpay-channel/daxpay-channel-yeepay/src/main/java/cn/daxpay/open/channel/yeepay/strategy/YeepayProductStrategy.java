package cn.daxpay.open.channel.yeepay.strategy;

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

/// # 易宝聚合支付产品策略
///
/// 易宝为聚合支付通道, 单产品(YEE_PAY)承载微信/支付宝/银联的扫码与 H5 支付。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.AGGREGATE_PAY_QRCODE, List.of(PayCapabilityEnum.AGGREGATE_PAY_QRCODE)),
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_PAY_QR)),
            Map.entry(PayMethodEnum.WECHAT_H5, List.of(PayCapabilityEnum.WECHAT_H5)),
            Map.entry(PayMethodEnum.ALIPAY_H5, List.of(PayCapabilityEnum.ALIPAY_H5)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YEE_PAY;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public boolean isSandbox() {
        return true;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(WECHAT, ALIPAY, UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
