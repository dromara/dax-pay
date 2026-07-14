package cn.daxpay.open.channel.ums.strategy;

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

/// # 银联商务C扫B支付（主扫）产品策略
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsQrcodeProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.of(
            PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR),
            PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR),
            PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_PAY_QR));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_QRCODE;
    }

    @Override
    public boolean isTerminal() { return true; }

    @Override
    public boolean isSandbox() { return true; }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY, PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
