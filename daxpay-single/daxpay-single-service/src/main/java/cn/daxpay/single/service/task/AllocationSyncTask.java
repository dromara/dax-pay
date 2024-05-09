package cn.daxpay.single.service.task;

import cn.daxpay.single.code.AllocOrderStatusEnum;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrder;
import cn.daxpay.single.service.core.payment.allocation.service.AllocationService;
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
public class AllocationSyncTask implements Job {

    private final AllocationOrderManager allocationOrderManager;

    private final AllocationService allocationService;

    /**
     * 分账同步
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        for (AllocationOrder allocationOrder : allocationOrderManager.findSyncOrder()) {
            try {
                // 分账中走同步逻辑
                if (allocationOrder.getStatus().equals(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())) {
                    allocationService.sync(allocationOrder);
                }
                // 如果分账结束, 调用自动完结逻辑
                if (allocationOrder.getStatus().equals(AllocOrderStatusEnum.ALLOCATION_END.getCode())) {
                    allocationService.finish(allocationOrder);
                }
            } catch (Exception e) {
                log.warn("分账同步或完结失败, 分账号:{}", allocationOrder.getAllocationNo());
                log.warn("分账同步或完结失败", e);
            }
        }

    }
}
