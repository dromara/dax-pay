package cn.bootx.platform.daxpay.service.core.timeout.task;

import cn.bootx.platform.daxpay.param.pay.PaySyncParam;
import cn.bootx.platform.daxpay.service.core.payment.sync.service.PaySyncService;
import cn.bootx.platform.daxpay.service.core.timeout.dao.PayExpiredTimeRepository;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author xxm
 * @since 2024/1/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayExpiredTimeTask {
    private final PayExpiredTimeRepository repository;

    private final PaySyncService paySyncService;

    private final LockTemplate lockTemplate;

    /**
     * 先使用定时任务实现, 五秒轮训一下
     *
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void task(){
        log.debug("执行超时取消任务....");
        Set<String> expiredKeys = repository.getExpiredKeys(LocalDateTime.now());
        for (String expiredKey : expiredKeys) {
            LockInfo lock = lockTemplate.lock("payment:expired:" + expiredKey,10000,0);
            if (Objects.isNull(lock)){
                log.warn("支付同步处理中，执行下一个");
                continue;
            }
            try {
                // 执行同步操作, 网关同步时会对支付的进行状态的处理
                Long paymentId = Long.parseLong(expiredKey);
                PaySyncParam paySyncParam = new PaySyncParam();
                paySyncParam.setPaymentId(paymentId);
                paySyncService.sync(paySyncParam);
            } catch (Exception e) {
                log.error("超时取消任务 异常", e);
            } finally {
                lockTemplate.releaseLock(lock);
            }

        }



    }
}
