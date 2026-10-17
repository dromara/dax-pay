package cn.daxpay.open.channel.sand.strategy.product;

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

/// # 杉德支付产品策略
///
/// 杉德为直连商户聚合通道(SAND_PAY), 单产品覆盖微信/支付宝/云闪付三类底层渠道:
/// 扫码为分渠道二维码(payType 区分, 非一码多渠道聚合, 故无 aggregate_pay_qrcode 能力),
/// JSAPI/小程序经 payerInfo 上送用户标识。
@Slf4j
@Service
@RequiredArgsConstructor
public class SandDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射(杉德支持的支付方式)
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 微信(扫码/JSAPI/小程序)
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            // 支付宝(扫码/JSAPI)
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            // 云闪付(扫码)
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SAND_PAY;
    }

    /// 直连商户模式(策略走 direct 分支)
    @Override
    public boolean isIsv() {
        return false;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    /// 杉德有独立 UAT 沙箱网关(openapi-uat01.sand.com.cn)
    @Override
    public boolean isSandbox() {
        return true;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY, PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }

    /// 杉德通道: 微信侧仅 JSAPI/小程序支付需要 appid(随单上送 subAppId),
    /// 其余微信能力(扫码)与支付宝/云闪付能力无需绑定应用, 收窄弹窗候选与支付链路的解析范围
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of(PayCapabilityEnum.WECHAT_JSAPI, PayCapabilityEnum.WECHAT_MINI);
    }
}
