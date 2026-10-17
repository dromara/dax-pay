package cn.daxpay.open.channel.longpay.strategy.product;

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

/// # 建行龙支付产品策略
///
/// 建行为聚合支付通道(龙支付聚合码), 单产品(LONG_PAY)承载微信/支付宝/银联/建行钱包的聚合主扫:
/// 一张聚合码由建行侧自动识别底层渠道, 故四种扫码方式共用同一聚合码能力。
@Slf4j
@Service
@RequiredArgsConstructor
public class LongpayDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.AGGREGATE_PAY_QRCODE, List.of(PayCapabilityEnum.AGGREGATE_PAY_QRCODE)),
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LONG_PAY;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    /// 建行无公开沙箱网关
    @Override
    public boolean isSandbox() {
        return false;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        // AGGREGATE_PAY: 通道路由基础模式可按聚合扫码 method 绑定本产品
        return List.of(PayProviderEnum.AGGREGATE_PAY, PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY,
                PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
