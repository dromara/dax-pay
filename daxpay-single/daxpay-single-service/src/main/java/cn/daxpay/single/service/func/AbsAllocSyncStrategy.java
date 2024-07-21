package cn.daxpay.single.service.func;

import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.core.payment.sync.result.AllocRemoteSyncResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

    private List<AllocOrderDetail> allocOrderDetails;

    /**
     * 初始化参数
     */
    public void initParam(AllocOrder allocOrder, List<AllocOrderDetail> allocOrderDetails) {
        this.allocOrder = allocOrder;
        this.allocOrderDetails = allocOrderDetails;
    }

    /**
     * 同步状态
     */
    public abstract AllocRemoteSyncResult doSync();
}
