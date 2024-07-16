package cn.daxpay.single.service.func;

import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.payment.sync.result.AllocRemoteSyncResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 分账同步策略
 * @author xxm
 * @since 2024/7/16
 */
@Slf4j
@Getter
@Setter
public abstract class AbsAllocSyncStrategy implements PayStrategy{

    private AllocOrder allocOrder;

    /**
     * 初始化参数
     */
    public void initParam(AllocOrder allocOrder) {
        this.allocOrder = allocOrder;
    }

    /**
     * 同步状态
     */
    public abstract AllocRemoteSyncResult doSync();
}
