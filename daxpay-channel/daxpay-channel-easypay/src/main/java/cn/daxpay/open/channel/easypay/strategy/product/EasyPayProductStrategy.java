package cn.daxpay.open.channel.easypay.strategy.product;

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

/// # 易支付支付产品策略
///
/// 易支付三方聚合平台, 一通道一产品(EASY_PAY), 一期仅微信/支付宝扫码两类支付方式
/// (2026-09-18 自 3.0 商业版移植拍板; jsapi 族依赖通道级微信授权, 授权体系无通道档位, 后续增量)。
///
/// 易支付不提供集测环境, 不支持沙箱, 全部交易走配置中的平台地址。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 微信扫码
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            // 支付宝扫码
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EASY_PAY;
    }

    @Override
    public boolean isIsv() { return false; }

    @Override
    public boolean isTerminal() { return false; }

    /// 易支付不提供集测环境, 恒为生产环境
    @Override
    public boolean isSandbox() { return false; }

    @Override
    public ChannelApiCallMode getApiCallMode() { return ChannelApiCallMode.MCH; }

    @Override
    public ChannelPayIdType getPayIdType() { return ChannelPayIdType.MCH; }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
