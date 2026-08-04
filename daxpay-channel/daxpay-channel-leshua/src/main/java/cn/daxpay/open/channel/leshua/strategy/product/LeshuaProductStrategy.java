package cn.daxpay.open.channel.leshua.strategy.product;

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
import java.util.Set;

/// # 乐刷支付产品策略
///
/// 乐刷为聚合服务商通道, 支持微信/支付宝/云闪付三种底层渠道的扫码/JSAPI/小程序/付款码支付。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.WECHAT_BARCODE, List.of(PayCapabilityEnum.WECHAT_BARCODE)),
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            Map.entry(PayMethodEnum.UNION_BARCODE, List.of(PayCapabilityEnum.UNION_BARCODE)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            Map.entry(PayMethodEnum.UNION_JSAPI, List.of(PayCapabilityEnum.UNION_JSAPI)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LESHUA_PAY;
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
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY, PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }

    /// 乐刷聚合服务商: 微信侧仅 JSAPI/小程序支付需要 appid(拉起微信收银台),
    /// 其余微信能力(扫码/付款码等)无需绑定应用, 收窄弹窗候选与支付链路的解析范围
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of(PayCapabilityEnum.WECHAT_JSAPI, PayCapabilityEnum.WECHAT_MINI);
    }
}
