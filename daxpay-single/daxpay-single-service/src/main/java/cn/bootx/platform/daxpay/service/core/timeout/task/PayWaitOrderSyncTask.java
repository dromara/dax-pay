package cn.bootx.platform.daxpay.service.core.timeout.task;

import cn.bootx.platform.daxpay.param.pay.PaySyncParam;
import cn.bootx.platform.daxpay.service.core.payment.sync.service.PaySyncService;
import cn.bootx.platform.daxpay.service.core.timeout.dao.PayExpiredTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 *
 * @author xxm
 * @since 2024/1/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayWaitOrderSyncTask {
    private final PayExpiredTimeRepository repository;

    private final PaySyncService paySyncService;

    public void task(){
        log.info("开始同步支付订单");
        // 从超时订单列表中获取到未超时的订单号
        Set<String> keys = repository.getNormalKeysBy30Day();
        for (String key : keys) {
            log.info("key:{}", key);
            try {
                Long paymentId = Long.parseLong(key);
                PaySyncParam paySyncParam = new PaySyncParam();
                paySyncParam.setPaymentId(paymentId);
                // 执行网关同步, 网关同步时会对支付的进行状态的处理
                paySyncService.sync(paySyncParam);
            } catch (Exception e) {
                log.error("同步支付订单异常", e);
            }
        }

    }
}
