package cn.daxpay.single.service.core.payment.pay.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.payment.pay.dao.PayExpiredTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * 支付超时处理
 * @author xxm
 * @since 2024/1/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayExpiredTimeService {
    private final PayExpiredTimeRepository repository;

    /**
     * 支付单超时关闭事件注册, 失败重试3次, 间隔一秒
     */
    @Async("bigExecutor")
    @Retryable(value = RetryableException.class)
    public void registerExpiredTime(PayOrder payOrder) {
        LocalDateTime expiredTime = payOrder.getExpiredTime();
        // 支付中且有超时时间才会注册超时关闭事件
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode()) && Objects.nonNull(expiredTime)) {
            try {
                // 将过期时间添加到redis中, 往后延时一分钟
                expiredTime = LocalDateTimeUtil.offset(expiredTime, 1, ChronoUnit.MINUTES);
                repository.store(payOrder.getOrderNo(), expiredTime);
            }
            catch (Exception e) {
                log.error("注册支付单超时关闭失败",e);
                throw new RetryableException();
            }
        }
    }

    /**
     * 取消支付单超时关闭事件
     */
    public void cancelExpiredTime(String orderNo) {
        repository.removeKeys(orderNo);
    }

}
