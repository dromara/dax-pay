package cn.daxpay.open.channel.union.strategy.product;

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

/// # 云闪付产品策略
///
/// 云闪付为单一产品(直连银联 ACP), 含主扫([PayMethodEnum#UNION_QR]) / H5([PayMethodEnum#UNION_H5])
/// / 被扫([PayMethodEnum#UNION_BARCODE]) 三种支付方式, 方式→能力一对一同码。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.of(
            PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR),
            PayMethodEnum.UNION_H5, List.of(PayCapabilityEnum.UNION_H5),
            PayMethodEnum.UNION_BARCODE, List.of(PayCapabilityEnum.UNION_BARCODE));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_PAY;
    }

    @Override
    public boolean isSandbox() {
        return true;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
