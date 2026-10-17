package cn.daxpay.open.channel.llianpay.strategy.product;

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

/// # 连连聚合支付产品策略
///
/// 连连为聚合通道, 单产品(LLIAN_PAY)承载微信/支付宝/云闪付:
/// 扫码(微信/支付宝/云闪付)、JSAPI(微信公众号/小程序/支付宝 JS)、
/// 收银台跳转(union_jsapi/union_h5, 连连收银台承载云闪付/快捷等银行渠道)。
///
/// 连连提供测试网关(mpay-openapi-ste2), 支持沙箱环境, 生产/沙箱密钥双环境并存。
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpayDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 扫码族
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            // JSAPI 族
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            // 银联收银台跳转(连连收银台承载云闪付/快捷)
            Map.entry(PayMethodEnum.UNION_JSAPI, List.of(PayCapabilityEnum.UNION_JSAPI)),
            Map.entry(PayMethodEnum.UNION_H5, List.of(PayCapabilityEnum.UNION_H5)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LLIAN_PAY;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    /// 连连提供测试网关(mpay-openapi-ste2.lianlianpay.com), 支持沙箱环境
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

    /// 连连聚合: 微信侧仅 JSAPI/小程序支付需要 appid(wx_data.appid),
    /// 其余微信能力(扫码等)无需绑定应用, 收窄弹窗候选与支付链路的解析范围
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of(PayCapabilityEnum.WECHAT_JSAPI, PayCapabilityEnum.WECHAT_MINI);
    }
}
