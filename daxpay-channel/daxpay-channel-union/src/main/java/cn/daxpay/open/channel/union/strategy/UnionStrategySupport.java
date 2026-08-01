package cn.daxpay.open.channel.union.strategy;

import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.experimental.UtilityClass;

/// # 云闪付策略支持
///
/// 云闪付为单一渠道(UNION_PAY), 支付方式直接由所属产品决定:
/// UNION_QRCODE→QRCODE / UNION_H5→H5 / UNION_BARCODE→BARCODE。
@UtilityClass
public class UnionStrategySupport {

    /// 产品 → 通道支付方式
    public UnionPayMethod resolveMethod(ProductEnum product) {
        return switch (product) {
            case UNION_QRCODE -> UnionPayMethod.QRCODE;
            case UNION_H5 -> UnionPayMethod.H5;
            case UNION_BARCODE -> UnionPayMethod.BARCODE;
            default -> throw new IllegalStateException("非云闪付产品: " + product);
        };
    }
}
