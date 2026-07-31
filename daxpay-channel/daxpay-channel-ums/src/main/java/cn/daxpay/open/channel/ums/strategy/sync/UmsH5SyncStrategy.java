package cn.daxpay.open.channel.ums.strategy.sync;

import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.sync.UmsSyncService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 银联商务(H5)支付同步策略
///
/// 对应 [ProductEnum#UMS_H5], 共享 [AbsUmsSyncStrategy] 的查单执行逻辑。
@Service
public class UmsH5SyncStrategy extends AbsUmsSyncStrategy {

    public UmsH5SyncStrategy(UmsSyncService umsSyncService, UmsDirectConfigAssembler umsDirectConfigAssembler) {
        super(umsSyncService, umsDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_H5;
    }
}
