package cn.daxpay.open.channel.umfpay.strategy.product;

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

/// # 联动优势聚合支付产品策略
///
/// 联动优势为聚合支付通道(老网关 payservice.do 表单线), 单产品(UMF_PAY)承载:
/// 微信/支付宝/云闪付扫码(scancode_type 指定底层渠道)与微信公众号跳转(publicnumber_and_verticalcode)。
/// 注意: 联动优势扫码需按渠道指定 scancode_type, 不支持一码多渠道的聚合扫码(AGGREGATE_PAY_QRCODE)。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmfpayDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 扫码族(scancode_type 指定底层渠道)
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            // 微信公众号支付(GET 跳转链接)
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMF_PAY;
    }

    /// 联动优势无独立沙箱网关
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
