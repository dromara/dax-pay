package cn.bootx.platform.daxpay.service.core.payment.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.param.pay.AllocationSyncParam;
import cn.bootx.platform.daxpay.result.pay.SyncResult;
import cn.bootx.platform.daxpay.service.core.order.allocation.dao.AllocationOrderManager;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrder;
import cn.bootx.platform.daxpay.service.core.payment.allocation.factory.AllocationSyncFactory;
import cn.bootx.platform.daxpay.service.func.AbsAllocationSyncStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 分账状态同步
 * @author xxm
 * @since 2024/4/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationSyncService {
    private final AllocationOrderManager allocationOrderManager;

    /**
     * 支付同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public SyncResult sync(AllocationSyncParam param) {
        // 获取分账订单
        AllocationOrder allocationOrder = null;
        if (Objects.nonNull(param.getAllocationId())){
            allocationOrder = allocationOrderManager.findById(param.getAllocationId())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        if (Objects.isNull(allocationOrder)){
            allocationOrder = allocationOrderManager.findByAllocationNo(param.getAllocationNo())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        // 获取分账策略
        AbsAllocationSyncStrategy allocationSyncStrategy = AllocationSyncFactory.create(allocationOrder.getChannel());
        allocationSyncStrategy.initPayParam(allocationOrder);
        allocationSyncStrategy.doSyncStatus();
        return new SyncResult();
    }
}
