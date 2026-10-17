package cn.daxpay.open.channel.jeepay.strategy.product;

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

/// # Jeepay支付产品策略
///
/// Jeepay 计全三方聚合平台, 一通道一产品(JEE_PAY), 一期 12 项支付方式
/// (微信/支付宝/云闪付的扫码+JSAPI 族, 对齐 pay_md_product_capability 主数据)。
///
/// Jeepay 不提供集测环境, 不支持沙箱, 全部交易走配置中的平台地址。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepayProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 支付宝扫码
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            // 支付宝生活号/JSAPI
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            // 支付宝PC网站
            Map.entry(PayMethodEnum.ALIPAY_PC, List.of(PayCapabilityEnum.ALIPAY_PC)),
            // 支付宝WAP/H5
            Map.entry(PayMethodEnum.ALIPAY_H5, List.of(PayCapabilityEnum.ALIPAY_H5)),
            // 支付宝APP
            Map.entry(PayMethodEnum.ALIPAY_APP, List.of(PayCapabilityEnum.ALIPAY_APP)),
            // 微信扫码(Native)
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            // 微信公众号JSAPI
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            // 微信小程序
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            // 微信H5
            Map.entry(PayMethodEnum.WECHAT_H5, List.of(PayCapabilityEnum.WECHAT_H5)),
            // 微信APP
            Map.entry(PayMethodEnum.WECHAT_APP, List.of(PayCapabilityEnum.WECHAT_APP)),
            // 云闪付扫码
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            // 云闪付JSAPI
            Map.entry(PayMethodEnum.UNION_JSAPI, List.of(PayCapabilityEnum.UNION_JSAPI)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JEE_PAY;
    }

    @Override
    public boolean isIsv() { return false; }

    @Override
    public boolean isTerminal() { return false; }

    /// Jeepay 不提供集测环境, 恒为生产环境
    @Override
    public boolean isSandbox() { return false; }

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
}
