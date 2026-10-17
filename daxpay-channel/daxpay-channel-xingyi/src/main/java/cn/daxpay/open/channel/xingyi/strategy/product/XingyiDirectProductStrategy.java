package cn.daxpay.open.channel.xingyi.strategy.product;

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

/// # 星驿付支付产品策略
///
/// 星驿付(新大陆旗下国通星驿)为聚合通道, 单产品(XINGYI_PAY)承载微信/支付宝/云闪付:
/// 扫码(支付宝官方码/云闪付官方码/微信星驿码)、JSAPI(微信/支付宝)、微信小程序、付款码(微信/支付宝/云闪付)。
///
/// 星驿付提供测试网关(xyf-server-test), 支持沙箱环境, 生产/沙箱密钥双环境并存。
@Slf4j
@Service
@RequiredArgsConstructor
public class XingyiDirectProductStrategy extends AbsProductStrategy {

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
            // 付款码族
            Map.entry(PayMethodEnum.WECHAT_BARCODE, List.of(PayCapabilityEnum.WECHAT_BARCODE)),
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            Map.entry(PayMethodEnum.UNION_BARCODE, List.of(PayCapabilityEnum.UNION_BARCODE)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.XINGYI_PAY;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    /// 星驿付提供测试网关(xyf-server-test.postar.cn), 支持沙箱环境
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
}
