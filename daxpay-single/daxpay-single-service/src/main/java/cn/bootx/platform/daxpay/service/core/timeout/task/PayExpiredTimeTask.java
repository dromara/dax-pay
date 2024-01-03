package cn.bootx.platform.daxpay.service.core.timeout.task;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.service.core.record.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.timeout.dao.PayExpiredTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final PayOrderManager payOrderManager;

//    @Scheduled(cron = "*/5 * * * * ?")
    public void task(){
        log.info("执行....");
        Set<String> expiredKeys = repository.getExpiredKeys(LocalDateTime.now());
        for (String expiredKey : expiredKeys) {
            log.info("key:{}", expiredKey);
            try {
                // 查询对应的订单
                PayOrder payOrder = payOrderManager.findById(Long.parseLong(expiredKey))
                        .orElseThrow(() -> new DataNotExistException("支付单未找到"));
                // 调用订单同步逻辑. 结果如果不是支付中, 进行补偿处理

                // 如果状态是支付中, 则进行支付超时处理
            } catch (Exception e) {
                log.error("超时任务异常", e);
            }

        }



    }
}
