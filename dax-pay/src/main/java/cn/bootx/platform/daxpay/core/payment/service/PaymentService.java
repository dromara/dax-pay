package cn.bootx.platform.daxpay.core.payment.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.payment.dao.PaymentExpiredTimeRepository;
import cn.bootx.platform.daxpay.core.payment.dao.PaymentManager;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.dto.payment.RefundableInfo;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.bootx.platform.daxpay.code.pay.PayStatusCode.TRADE_PROGRESS;

/**
 * 支付记录
 *
 * @author xxm
 * @since 2021/3/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentManager paymentManager;

    private final PaymentExpiredTimeRepository expiredTimeRepository;

    /**
     * 保存
     */
    public Payment save(Payment payment) {
        return paymentManager.save(payment);
    }

    /**
     * 更新支付记录
     */
    public Payment updateById(Payment payment) {
        // 超时注册
        this.registerExpiredTime(payment);
        return paymentManager.updateById(payment);
    }

    /**
     * 根据id查询
     */
    public Optional<Payment> findById(Serializable id) {
        return paymentManager.findById(id);
    }

    /**
     * 根据BusinessId查询
     */
    public Optional<Payment> findByBusinessId(String businessId) {
        return paymentManager.findByBusinessId(businessId);
    }

    /**
     * 退款成功处理, 更新可退款信息 不要进行持久化
     */
    public void updateRefundSuccess(Payment payment, BigDecimal amount, PayChannelEnum payChannelEnum) {
        // 删除旧有的退款记录, 替换退款完的新的
        List<RefundableInfo> refundableInfos = payment.getRefundableInfo();
        RefundableInfo refundableInfo = refundableInfos.stream()
            .filter(o -> Objects.equals(o.getPayChannel(), payChannelEnum.getCode()))
            .findFirst()
            .orElseThrow(() -> new PayFailureException("数据不存在"));
        refundableInfos.remove(refundableInfo);
        refundableInfo.setAmount(refundableInfo.getAmount().subtract(amount));
        refundableInfos.add(refundableInfo);
        payment.setRefundableInfo(refundableInfos);
    }

    /**
     * 支付单超时关闭事件注册, 失败重试3次, 间隔一秒
     */
    @Async("bigExecutor")
    @Retryable(value = RetryableException.class)
    public void registerExpiredTime(Payment payment) {
        LocalDateTime expiredTime = payment.getExpiredTime();
        // 支付中且有超时时间才会注册超时关闭时间
        if (Objects.equals(payment.getPayStatus(), TRADE_PROGRESS) && Objects.nonNull(expiredTime)) {
            try {
                // 将过期时间添加到redis中, 往后延时一分钟
                expiredTime = LocalDateTimeUtil.offset(expiredTime, 1, ChronoUnit.MINUTES);
                expiredTimeRepository.store(payment.getId(), expiredTime);
            }
            catch (Exception e) {
                log.error("注册支付单超时关闭失败");
                throw new RetryableException();
            }
        }
    }

}
