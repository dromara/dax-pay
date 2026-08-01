package cn.daxpay.open.channel.union.strategy.refund;

import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.refund.UnionRefundService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 云闪付(B扫C)退款策略
@Service
public class UnionBarcodeRefundStrategy extends AbsUnionRefundStrategy {

    public UnionBarcodeRefundStrategy(UnionRefundService unionRefundService, UnionDirectConfigAssembler unionDirectConfigAssembler) {
        super(unionRefundService, unionDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_BARCODE;
    }
}
