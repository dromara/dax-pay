package cn.daxpay.open.channel.union.strategy.refund;

import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.refund.UnionRefundSyncService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 云闪付(C扫B)退款同步策略
@Service
public class UnionQrcodeSyncRefundStrategy extends AbsUnionSyncRefundStrategy {

    public UnionQrcodeSyncRefundStrategy(UnionRefundSyncService unionRefundSyncService, UnionDirectConfigAssembler unionDirectConfigAssembler) {
        super(unionRefundSyncService, unionDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_QRCODE;
    }
}
