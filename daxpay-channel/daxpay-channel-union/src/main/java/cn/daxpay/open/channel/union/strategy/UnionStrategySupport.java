package cn.daxpay.open.channel.union.strategy;

import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import lombok.experimental.UtilityClass;

/// # 云闪付策略支持
///
/// 云闪付为单一产品(UNION_PAY), 通道支付方式由订单/退款单上的 capability 决定
/// (与 [PayCapabilityEnum] 银联系列同码): union_qr→QRCODE / union_h5→H5 / union_barcode→BARCODE
@UtilityClass
public class UnionStrategySupport {

    /// 支付能力 code → 银联 ACP 通道支付方式
    public UnionPayMethod resolveMethod(String capabilityCode) {
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(capabilityCode);
        if (cap == null) {
            throw new IllegalStateException("非云闪付支付能力: " + capabilityCode);
        }
        return switch (cap) {
            case UNION_QR -> UnionPayMethod.QRCODE;
            case UNION_H5 -> UnionPayMethod.H5;
            case UNION_BARCODE -> UnionPayMethod.BARCODE;
            default -> throw new IllegalStateException("非云闪付支付能力: " + capabilityCode);
        };
    }
}
