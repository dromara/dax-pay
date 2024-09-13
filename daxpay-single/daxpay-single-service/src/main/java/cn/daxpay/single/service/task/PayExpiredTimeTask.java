package cn.daxpay.single.service.task;

import cn.daxpay.single.core.param.payment.pay.PaySyncParam;
import cn.daxpay.single.service.core.payment.pay.dao.PayExpiredTimeRepository;
import cn.daxpay.single.service.core.payment.sync.service.PaySyncService;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * 支付超时处理(手动注册)
 * @author xxm
 * @since 2024/1/2
 */
@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class PayExpiredTimeTask implements Job {
    private final PayExpiredTimeRepository repository;

    private final PaySyncService paySyncService;

    private final LockTemplate lockTemplate;

    @Override
    public void execute(JobExecutionContext context) {
        // 获取超时的任务Key
        Set<String> expiredKeys = repository.getExpiredKeys(LocalDateTime.now());
        for (String orderNo : expiredKeys) {
            LockInfo lock = lockTemplate.lock("payment:expired:" + orderNo,10000,200);
            if (Objects.isNull(lock)){
                log.warn("支付同步处理中，执行下一个");
                continue;
            }
            try {
                // 执行同步操作, 网关同步时会对支付的进行状态的处理
                PaySyncParam paySyncParam = new PaySyncParam();
                paySyncParam.setOrderNo(orderNo);
                paySyncService.sync(paySyncParam);
            } catch (Exception e) {
                // 如果是未查询到取消支付订单, 则删除这个任务 TODO 后续调整为专用的异常类
                if (Objects.equals("支付订单不存在", e.getMessage())){
                    repository.removeKeys(orderNo);
                }
                log.error("超时取消任务 异常", e);
            } finally {
                lockTemplate.releaseLock(lock);
            }
        }
    }
}
