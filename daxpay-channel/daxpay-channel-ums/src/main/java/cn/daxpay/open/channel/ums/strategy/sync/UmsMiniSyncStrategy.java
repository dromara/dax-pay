package cn.daxpay.open.channel.ums.strategy.sync;

import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.sync.UmsSyncService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 银联商务(小程序)支付同步策略
///
/// 对应 [ProductEnum#UMS_MINI], 共享 [AbsUmsSyncStrategy] 的查单执行逻辑。
@Service
public class UmsMiniSyncStrategy extends AbsUmsSyncStrategy {

    public UmsMiniSyncStrategy(UmsSyncService umsSyncService, UmsDirectConfigAssembler umsDirectConfigAssembler) {
        super(umsSyncService, umsDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_MINI;
    }
}
