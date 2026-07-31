package cn.daxpay.open.channel.ums.strategy.refund;

import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.refund.UmsRefundSyncService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 银联商务(小程序)退款同步策略
///
/// 对应 [ProductEnum#UMS_MINI], 共享 [AbsUmsSyncRefundStrategy] 的退款查询执行逻辑。
@Service
public class UmsMiniSyncRefundStrategy extends AbsUmsSyncRefundStrategy {

    public UmsMiniSyncRefundStrategy(UmsRefundSyncService umsRefundSyncService, UmsDirectConfigAssembler umsDirectConfigAssembler) {
        super(umsRefundSyncService, umsDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_MINI;
    }
}
