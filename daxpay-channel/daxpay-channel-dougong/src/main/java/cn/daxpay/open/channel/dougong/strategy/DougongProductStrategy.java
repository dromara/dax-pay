package cn.daxpay.open.channel.dougong.strategy;

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

/// # 斗拱支付产品策略
///
/// 声明斗拱(汇付天下)为聚合服务商模式(ISV), 一个产品覆盖微信/支付宝/银联三类底层渠道。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射(斗拱支持的支付方式子集)
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 微信(扫码/JSAPI/小程序)
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            // 支付宝(扫码/JSAPI/小程序)
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_MINI, List.of(PayCapabilityEnum.ALIPAY_MINI)),
            // 银联(扫码)
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_PAY_QR)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUGONG_PAY;
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
        return false;
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
        return List.of(WECHAT, ALIPAY, UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
