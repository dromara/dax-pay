package cn.bootx.platform.daxpay.service.core.payment.sync.task;

import cn.bootx.platform.daxpay.service.core.payment.sync.service.PaySyncService;
import cn.bootx.platform.daxpay.param.pay.PaySyncParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 对未过期的支付中订单进行状态同步
 * @author xxm
 * @since 2024/1/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderSyncTaskService {
    private final cn.bootx.platform.daxpay.service.core.timeout.dao.PayExpiredTimeRepository PayExpiredTimeRepository;

    private final PaySyncService paySyncService;

    /**
     * 同步支付订单任务
     */
    public void syncTask() {
        log.info("开始同步支付订单");
        // 1. 从超时订单列表中获取到未超时的订单号
        for (String s : PayExpiredTimeRepository.getNormalKeysBy7Day()) {
            try {
                Long paymentId = Long.parseLong(s);
                PaySyncParam paySyncParam = new PaySyncParam();
                paySyncParam.setPaymentId(paymentId);
                paySyncService.sync(paySyncParam);
            } catch (Exception e) {
                log.error("同步支付订单异常", e);
            }
        }
    }
}
