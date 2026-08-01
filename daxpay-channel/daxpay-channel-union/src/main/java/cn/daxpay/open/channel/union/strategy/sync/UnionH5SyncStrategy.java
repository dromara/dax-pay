package cn.daxpay.open.channel.union.strategy.sync;

import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.sync.UnionSyncService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 云闪付(H5)支付同步策略
@Service
public class UnionH5SyncStrategy extends AbsUnionSyncStrategy {

    public UnionH5SyncStrategy(UnionSyncService unionSyncService, UnionDirectConfigAssembler unionDirectConfigAssembler) {
        super(unionSyncService, unionDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_H5;
    }
}
