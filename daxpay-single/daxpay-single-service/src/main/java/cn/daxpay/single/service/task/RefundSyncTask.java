package cn.daxpay.single.service.task;

import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.payment.sync.service.RefundSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 退款定时同步任务 一分钟一次, 查询退款中的订单进行同步
 * @author xxm
 * @since 2024/3/12
 */
@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class RefundSyncTask implements Job {

    private final RefundSyncService refundSyncService;

    private final RefundOrderManager refundOrderManager;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 查询退款中的退款订单
        List<RefundOrder> list = refundOrderManager.findAllByProgress();
        for (RefundOrder refundOrder : list) {
            try {
                // 调用同步方法
                refundSyncService.syncRefundOrder(refundOrder);
            } catch (Exception e) {
                log.warn("退款执行同步失败, ID: {}",refundOrder.getId());
                log.warn("退款执行同步失败",e);
            }
        }
    }
}
