package cn.bootx.platform.daxpay.core.order.pay.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayExpiredTimeRepository;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderChannelManager;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderExtraManager;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.common.entity.OrderRefundableInfo;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 支付订单服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderService {

    private final PayOrderManager orderManager;

    private final PayOrderChannelManager orderChannelManager;

    private final PayOrderExtraManager orderExtraManager;

    private final PayExpiredTimeRepository expiredTimeRepository;

    /**
     * 保存
     */
    public PayOrder saveOder(PayOrder payment) {
        return orderManager.save(payment);
    }

    /**
     * 保存支付通道信息列表
     */
    public void saveOrderChannels(List<PayOrderChannel> payOrderChannels){
        orderChannelManager.saveAll(payOrderChannels);
    }

    /**
     * 更新支付记录
     */
    public PayOrder updateById(PayOrder payment) {
        // 超时注册
        this.registerExpiredTime(payment);
        return orderManager.updateById(payment);
    }


    /**
     * 根据id查询
     */
    public Optional<PayOrder> findById(Serializable id) {
        return orderManager.findById(id);
    }

    /**
     * 根据BusinessId查询
     */
    public Optional<PayOrder> findByBusinessId(String businessNo) {
        return orderManager.findByBusinessNo(businessNo);
    }

    /**
     * 退款成功处理, 更新可退款信息 不要进行持久化
     */
    public void updateRefundSuccess(PayOrder payment, int amount, PayChannelEnum payChannelEnum) {
        // 删除旧有的退款记录, 替换退款完的新的
        List<OrderRefundableInfo> refundableInfos = payment.getRefundableInfos();
        OrderRefundableInfo refundableInfo = refundableInfos.stream()
                .filter(o -> Objects.equals(o.getChannel(), payChannelEnum.getCode()))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("退款数据不存在"));
        refundableInfos.remove(refundableInfo);
        refundableInfo.setAmount(refundableInfo.getAmount() - amount);
        refundableInfos.add(refundableInfo);
        payment.setRefundableInfos(refundableInfos);
    }

    /**
     * 支付单超时关闭事件注册, 失败重试3次, 间隔一秒
     */
    @Async("bigExecutor")
    @Retryable(value = RetryableException.class)
    public void registerExpiredTime(PayOrder payOrder) {
        LocalDateTime expiredTime = payOrder.getExpiredTime();
        // 支付中且有超时时间才会注册超时关闭时间
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode()) && Objects.nonNull(expiredTime)) {
            try {
                // 将过期时间添加到redis中, 往后延时一分钟
                expiredTime = LocalDateTimeUtil.offset(expiredTime, 1, ChronoUnit.MINUTES);
                expiredTimeRepository.store(payOrder.getId(), expiredTime);
            }
            catch (Exception e) {
                log.error("注册支付单超时关闭失败");
                throw new RetryableException();
            }
        }
    }
}
