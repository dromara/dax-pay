package cn.daxpay.open.channel.fuyou.strategy.product;

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

/// # 富友互联网直连支付产品策略
///
/// 声明富友互联网直连模式(fuyou_internet, hlwnets/aggpcpay 网关, JSON + RSA 加密信封) +
/// 微信/支付宝/银联 的 JSAPI/扫码(无付款码, 互联网接口族无被扫接口)。
/// 与服务商模式产品([FuyouProductStrategy], FUYOU_PAY)共用通道 fuyou_pay, 多产品并存。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouInternetProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 微信
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            // 支付宝
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            // 银联
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_INTERNET;
    }

    @Override
    public boolean isIsv() { return false; }

    @Override
    public boolean isTerminal() { return false; }

    @Override
    public boolean isSandbox() { return true; }

    @Override
    public ChannelApiCallMode getApiCallMode() { return ChannelApiCallMode.MCH; }

    @Override
    public ChannelPayIdType getPayIdType() { return ChannelPayIdType.MCH; }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY, PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }

    /// 富友互联网直连: 微信侧仅 JSAPI/小程序支付可随单上送 appid,
    /// 其余微信能力(扫码)与支付宝/银联能力无需绑定应用, 收窄弹窗候选与支付链路的解析范围
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of(PayCapabilityEnum.WECHAT_JSAPI, PayCapabilityEnum.WECHAT_MINI);
    }
}
