package cn.daxpay.open.channel.shouqianba.strategy.product;

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

/// # 收钱吧聚合支付产品策略
///
/// 收钱吧为聚合扫码通道(SHOUQIANBA 单产品), 按 payway 分渠道下单:
/// 支付宝扫码(payway=2) / 微信扫码(payway=3) / 云闪付扫码(payway=17) / 微信 WAP 收银台。
/// 收钱吧无一张码自动识别的聚合模式, 不提供 AGGREGATE_PAY_QRCODE 能力。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShouqianbaDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            Map.entry(PayMethodEnum.WECHAT_H5, List.of(PayCapabilityEnum.WECHAT_H5)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHOUQIANBA;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    /// 收钱吧无独立沙箱网关
    @Override
    public boolean isSandbox() {
        return false;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        // 按 payway 分渠道下单, 支持微信/支付宝/云闪付三钱包(无聚合码模式)
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY, PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
