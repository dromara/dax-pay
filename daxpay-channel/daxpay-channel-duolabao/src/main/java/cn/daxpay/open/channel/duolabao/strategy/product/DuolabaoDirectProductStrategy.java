package cn.daxpay.open.channel.duolabao.strategy.product;

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

/// # 哆啦宝支付产品策略
///
/// 哆啦宝为聚合通道, 单产品(DUOLABAO_PAY)承载微信/支付宝/云闪付:
/// 扫码族(微信/支付宝/云闪付共用一张聚合码)、云闪付 H5(聚合码链接跳转)、
/// JSAPI 族(支付宝=买家用户ID / 微信=openid+appId)。
///
/// 哆啦宝无独立沙箱网关, 不支持沙箱环境。
@Slf4j
@Service
@RequiredArgsConstructor
public class DuolabaoDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 扫码族(一张聚合码)
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            // 云闪付 H5(聚合码链接跳转)
            Map.entry(PayMethodEnum.UNION_H5, List.of(PayCapabilityEnum.UNION_H5)),
            // JSAPI 族
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DUOLABAO_PAY;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    /// 哆啦宝无独立沙箱网关
    @Override
    public boolean isSandbox() {
        return false;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY, PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
