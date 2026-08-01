package cn.daxpay.open.channel.union.strategy.refund;

import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.refund.UnionRefundService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 云闪付(H5)退款策略
@Service
public class UnionH5RefundStrategy extends AbsUnionRefundStrategy {

    public UnionH5RefundStrategy(UnionRefundService unionRefundService, UnionDirectConfigAssembler unionDirectConfigAssembler) {
        super(unionRefundService, unionDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_H5;
    }
}
