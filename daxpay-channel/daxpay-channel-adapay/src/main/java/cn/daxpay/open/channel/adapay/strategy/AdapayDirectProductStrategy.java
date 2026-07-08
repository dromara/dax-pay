package cn.daxpay.open.channel.adapay.strategy;

import cn.daxpay.open.platform.core.enums.pay.channel.ChannelApiCallMode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelPayIdType;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.core.strategy.product.AbsProductStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum.ALIPAY;
import static cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum.UNION_PAY;
import static cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum.WECHAT;

/// # Adapay 直连支付产品策略
///
/// 声明 Adapay(ada_pay)为直连聚合模式, 一个产品覆盖微信/支付宝/银联三类底层渠道。
/// 支付方式与子应用 [cn.daxpay.open.channel.adapay.enums.AdapayPayMethod] (16 种) 对齐,
/// 与 `pay_md_product_capability` 预置数据(id 21017-21032)一致。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射(Adapay Adapay 支持的 16 种支付方式)
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 微信(扫码/JSAPI/APP/H5/小程序/付款码)
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_APP, List.of(PayCapabilityEnum.WECHAT_APP)),
            Map.entry(PayMethodEnum.WECHAT_H5, List.of(PayCapabilityEnum.WECHAT_H5)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.WECHAT_BARCODE, List.of(PayCapabilityEnum.WECHAT_BARCODE)),
            // 支付宝(扫码/JSAPI/APP/H5/PC/付款码)
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_APP, List.of(PayCapabilityEnum.ALIPAY_APP)),
            Map.entry(PayMethodEnum.ALIPAY_H5, List.of(PayCapabilityEnum.ALIPAY_H5)),
            Map.entry(PayMethodEnum.ALIPAY_PC, List.of(PayCapabilityEnum.ALIPAY_PC)),
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            // 银联(扫码/JSAPI/H5/付款码)
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_PAY_QR)),
            Map.entry(PayMethodEnum.UNION_JSAPI, List.of(PayCapabilityEnum.UNION_PAY_JSAPI)),
            Map.entry(PayMethodEnum.UNION_H5, List.of(PayCapabilityEnum.UNION_PAY_H5)),
            Map.entry(PayMethodEnum.UNION_PAY_BARCODE, List.of(PayCapabilityEnum.UNION_PAY_BARCODE)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ADA_PAY;
    }

    /// 直连模式: 商户自行持有Adapay 配置, 非服务商
    @Override
    public boolean isIsv() {
        return false;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    /// Adapay Adapay 提供测试环境(test_api_key)与生产环境(live_api_key)
    @Override
    public boolean isSandbox() {
        return true;
    }

    /// 标准商户调用: 直连商户持有全部调用配置
    @Override
    public ChannelApiCallMode getApiCallMode() {
        return ChannelApiCallMode.MCH;
    }

    @Override
    public ChannelPayIdType getPayIdType() {
        return ChannelPayIdType.MCH;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(WECHAT, ALIPAY, UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
