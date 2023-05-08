package cn.bootx.daxpay.task;

import cn.bootx.common.core.util.CollUtil;
import cn.bootx.daxpay.core.payment.dao.PaymentExpiredTimeRepository;
import cn.bootx.daxpay.mq.PaymentEventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 支付超时任务撤销消息注册
 *
 * @author xxm
 * @date 2022/7/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayExpiredTimeTaskService {

    private final PaymentExpiredTimeRepository expiredTimeRepository;

    private final PaymentEventSender paymentEventSender;

    /**
     * 定时查询, 如果有过时的发送到消息队列
     */
    public void sync() {
        List<Long> paymentIds = expiredTimeRepository.retrieveExpiredKeys(LocalDateTime.now())
            .stream()
            .map(Long::valueOf)
            .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(paymentIds)) {
            expiredTimeRepository.removeKeys(paymentIds.stream().map(String::valueOf).toArray(String[]::new));
            paymentIds.forEach(paymentEventSender::sendPaymentExpiredTime);
        }
    }

}
