package cn.daxpay.single.service.task;

import cn.daxpay.single.service.core.order.pay.dao.PayOrderManager;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.payment.sync.service.PaySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/5/17
 */
@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class PayExpiredByDbTimeTask implements Job {

    private final PaySyncService paySyncService;

    private final PayOrderManager payOrderManager;

    @Override
    public void execute(JobExecutionContext context) {
        // 从数据库查询获取超时的任务对象
        List<PayOrder> payOrders = payOrderManager.queryExpiredOrder();
        for (PayOrder order : payOrders) {
            try {
                // 设置补偿来源为定时任务
                paySyncService.syncPayOrder(order);
            } catch (Exception e) {
                log.error("超时取消任务 异常", e);
            }
        }
    }
}
