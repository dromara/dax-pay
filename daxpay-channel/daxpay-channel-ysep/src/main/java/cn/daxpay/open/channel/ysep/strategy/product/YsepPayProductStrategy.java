package cn.daxpay.open.channel.ysep.strategy.product;

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

/// # 银盛支付产品策略(ysep_pay, 开放平台)
///
/// 银盛开放平台聚合支付(一通道两产品之一), 微信/支付宝/银联三钱包 8 种支付方式。
///
/// 注意: SQL 种子(pay_md_product_capability)含 16 项能力, 本映射仅覆盖开放平台接口族实际支持的 8 项
/// (alipay_h5=WAP 跳转, wechat_h5/barcode/app 族与 alipay_app/pc/barcode、union_h5/barcode 未接入),
/// 若商户配置了未覆盖能力路由, 支付时在支付方式映射处报不支持。
///
/// 银盛不提供集测沙箱网关, 不支持沙箱环境, 全部交易走生产网关。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepPayProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 微信(扫码/公众号/小程序)
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            // 支付宝(扫码/生活号 JS/WAP 跳转)
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_H5, List.of(PayCapabilityEnum.ALIPAY_H5)),
            // 银联(扫码/行业码 JS)
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            Map.entry(PayMethodEnum.UNION_JSAPI, List.of(PayCapabilityEnum.UNION_JSAPI)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YSEP_PAY;
    }

    @Override
    public boolean isIsv() { return false; }

    @Override
    public boolean isTerminal() { return false; }

    /// 银盛不提供集测沙箱网关, 恒为生产环境
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

    /// 银盛聚合通道: 仅微信 JSAPI/小程序支付需要 appid(随单上送),
    /// 其余微信能力(扫码/H5)与支付宝/银联能力无需绑定应用(对齐盛付通/乐刷范式)
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of(PayCapabilityEnum.WECHAT_JSAPI, PayCapabilityEnum.WECHAT_MINI);
    }
}
