package cn.daxpay.single.service.task;

import cn.daxpay.single.core.code.AllocOrderStatusEnum;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.payment.allocation.service.AllocationService;
import cn.daxpay.single.service.core.payment.sync.service.AllocSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * 自动分账信息同步和完结接口 10s一次
 * @author xxm
 * @since 2024/5/6
 */
@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class AllocSyncTask implements Job {

    private final AllocOrderManager allocOrderManager;

    private final AllocSyncService allocSyncService;
    private final AllocationService allocationService;

    /**
     * 分账同步
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        for (AllocOrder allocOrder : allocOrderManager.findSyncOrder()) {
            try {
                // 分账中走同步逻辑
                if (allocOrder.getStatus().equals(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())) {
                    allocSyncService.sync(allocOrder);
                }
                // 如果分账结束, 调用自动完结逻辑
                if (allocOrder.getStatus().equals(AllocOrderStatusEnum.ALLOCATION_END.getCode())) {
                    allocationService.finish(allocOrder);
                }
            } catch (Exception e) {
                log.warn("分账同步或完结失败, 分账号:{}", allocOrder.getAllocNo());
                log.warn("分账同步或完结失败", e);
            }
        }

    }
}
