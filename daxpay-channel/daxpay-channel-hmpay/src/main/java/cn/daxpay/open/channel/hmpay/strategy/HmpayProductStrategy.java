package cn.daxpay.open.channel.hmpay.strategy;

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

/// # 河马付支付产品策略
///
/// 声明河马付(杉德)为聚合服务商模式(ISV), 一个产品覆盖微信/支付宝两类底层渠道,
/// 支持通道原生聚合扫码; 付款码按分钱包 method 声明, 执行层折叠为统一条码 API。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射(河马付支持的支付方式)
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 聚合扫码(通道原生通扫码)
            Map.entry(PayMethodEnum.AGGREGATE_PAY_QRCODE, List.of(PayCapabilityEnum.AGGREGATE_PAY_QRCODE)),
            // 微信(扫码/JSAPI/小程序/付款码)
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.WECHAT_BARCODE, List.of(PayCapabilityEnum.WECHAT_BARCODE)),
            // 支付宝(扫码/JSAPI/小程序/付款码)
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_MINI, List.of(PayCapabilityEnum.ALIPAY_MINI)),
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HM_PAY;
    }

    @Override
    public boolean isIsv() {
        return true;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isSandbox() {
        return true;
    }

    @Override
    public ChannelApiCallMode getApiCallMode() {
        return ChannelApiCallMode.ISV;
    }

    @Override
    public ChannelPayIdType getPayIdType() {
        return ChannelPayIdType.MCH;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        // AGGREGATE_PAY: 通道路由基础模式可按聚合扫码 method 绑定本产品
        return List.of(PayProviderEnum.AGGREGATE_PAY, PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
