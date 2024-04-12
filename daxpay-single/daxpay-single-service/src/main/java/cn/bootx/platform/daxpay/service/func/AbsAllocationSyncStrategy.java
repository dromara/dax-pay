package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象的分账状态同步策略
 * @author xxm
 * @since 2024/4/12
 */
@Slf4j
@Getter
@Setter
public abstract class AbsAllocationSyncStrategy implements PayStrategy{

    private AllocationOrder allocationOrder;

    /**
     * 初始化参数
     */
    public void initPayParam(AllocationOrder allocationOrder) {
        this.allocationOrder = allocationOrder;
    }

    /**
     * 同步状态
     */
    public abstract void doSyncStatus();


}
