package cn.daxpay.open.channel.epaylinks.strategy.product;

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
import java.util.Set;

/// # 易票联聚合支付产品策略
///
/// 易票联为聚合支付通道(EFPS 统一网关), 单产品(EPAY_LINKS)承载微信/支付宝/云闪付:
/// 扫码(微信/支付宝/云闪付主扫, 按渠道分别出码)、JSAPI(微信公众号/小程序/支付宝生活号)、
/// 个人网银(统一收银台跳转)。
///
/// 易票联无独立沙箱网关, 不支持沙箱环境。
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 扫码族
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            // JSAPI 族
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            // 个人网银(收银台跳转)
            Map.entry(PayMethodEnum.UNION_H5, List.of(PayCapabilityEnum.UNION_H5)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EPAY_LINKS;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    /// 易票联无独立沙箱网关
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

    /// 易票联聚合: 微信侧仅 JSAPI/小程序支付需要 appId(WxJSAPIPayment 上送 appId),
    /// 其余微信能力(扫码等)无需绑定应用, 收窄弹窗候选与支付链路的解析范围
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of(PayCapabilityEnum.WECHAT_JSAPI, PayCapabilityEnum.WECHAT_MINI);
    }
}
