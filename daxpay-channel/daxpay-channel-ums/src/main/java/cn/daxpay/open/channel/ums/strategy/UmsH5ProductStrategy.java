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

import static cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum.ALIPAY;
import static cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum.UNION_PAY;
import static cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum.WECHAT;

/// # 银联商务H5支付产品策略
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsH5ProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.of(
            PayMethodEnum.WECHAT_H5, List.of(PayCapabilityEnum.WECHAT_H5),
            PayMethodEnum.ALIPAY_H5, List.of(PayCapabilityEnum.ALIPAY_H5),
            PayMethodEnum.UNION_H5, List.of(PayCapabilityEnum.UNION_PAY_H5));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_H5;
    }

    @Override
    public boolean isTerminal() { return true; }

    @Override
    public boolean isSandbox() { return true; }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(WECHAT, ALIPAY, UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
