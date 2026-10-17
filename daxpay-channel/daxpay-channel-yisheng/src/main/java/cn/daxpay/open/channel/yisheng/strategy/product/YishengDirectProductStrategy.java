package cn.daxpay.open.channel.yisheng.strategy.product;

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

/// # 易生支付产品策略
///
/// 易生为聚合支付通道, 单产品(YISHENG_PAY)承载微信/支付宝的扫码与 JSAPI 支付:
/// - 扫码: aliPay(支付宝)/wxNative(微信), 返回聚合无关的渠道二维码
/// - JSAPI: aliJsPay(支付宝, 上送买家 uid)/wxJsPay(微信, 上送 openid), 返回调起参数
/// 易生无需微信 appid(参考实现 jsapi 下单不使用 appid 参数), 不重写 wxAppRequiredCapabilities。
@Slf4j
@Service
@RequiredArgsConstructor
public class YishengDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YISHENG_PAY;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    /// 易生网关生产与沙箱同址, 无独立沙箱网关
    @Override
    public boolean isSandbox() {
        return false;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
