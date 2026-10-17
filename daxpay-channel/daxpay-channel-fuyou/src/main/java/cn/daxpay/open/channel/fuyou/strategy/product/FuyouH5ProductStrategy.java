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

/// # 富友 H5 聚合收银台支付产品策略
///
/// 声明富友 H5 聚合收银台模式(fuyou_h5, aggpcpay token 网关, JSON + RSA 加密信封) +
/// 支付宝扫码(AliPayJL) / 微信小程序(WeAppletPay) / 云闪付(AppQuickPay), 各方式均返回收银台链接 token_url。
/// 与服务商模式产品([FuyouProductStrategy], FUYOU_PAY)共用通道 fuyou_pay, 多产品并存。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouH5ProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 支付宝扫码(经收银台)
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            // 微信小程序(scheme 跳转)
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            // 银联云闪付 App(经收银台)
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_H5;
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

    /// 富友 H5 收银台: WeAppletPay 由富友侧小程序承接(参考实现不随单上送 appid),
    /// 全部能力无需绑定微信应用, 弹窗候选与支付链路解析范围收窄为空
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of();
    }
}
