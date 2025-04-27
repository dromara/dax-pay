package org.dromara.daxpay.service.service.gateway;

import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import org.dromara.daxpay.core.context.MchAppLocal;
import org.dromara.daxpay.core.enums.PayAllocStatusEnum;
import org.dromara.daxpay.core.enums.PayRefundStatusEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.param.gateway.GatewayPayParam;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.service.service.trade.pay.PayAssistService;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 收银台支撑服务
 * @author xxm
 * @since 2024/11/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayPayAssistService {
    private final DelayJobService delayJobService;
    private final PayOrderManager payOrderManager;
    private final PayOrderQueryService payOrderQueryService;
    private final PayAssistService payAssistService;

    /**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    public PayOrder getOrderAndCheck(String orderNo) {
        PayOrder payOrder = payOrderQueryService.findByOrderNo(orderNo).orElse(null);
        // 如果存在订单进行检查
        if (Objects.nonNull(payOrder)){
            payAssistService.checkOrder(payOrder);
        }
        return payOrder;
    }

    /**
     * 创建支付订单并保存, 返回支付订单
     */
    @Transactional(rollbackFor = Exception.class)
    public PayOrder createPayOrder(GatewayPayParam checkoutParam) {
        // 订单超时时间
        LocalDateTime expiredTime = payAssistService.getExpiredTime(checkoutParam.getExpiredTime());
        // 构建支付订单对象
        PayOrder order = new PayOrder();
        BeanUtil.copyProperties(checkoutParam, order);
        order.setOrderNo(TradeNoGenerateUtil.pay())
                .setStatus(PayStatusEnum.WAIT.getCode())
                .setRefundStatus(PayRefundStatusEnum.NO_REFUND.getCode())
                .setExpiredTime(expiredTime)
                .setRefundableBalance(checkoutParam.getAmount());
        // 如果支持分账, 设置分账状态为待分账
        if (order.getAllocation()) {
            order.setAllocStatus(PayAllocStatusEnum.WAITING.getCode());
        }
        payOrderManager.save(order);
        // 注册支付超时任务
        delayJobService.registerByTransaction(order.getId(), DaxPayCode.Event.ORDER_PAY_TIMEOUT, order.getExpiredTime());
        return order;
    }

    /**
     * 获取支付订单超时时间
     */
    private LocalDateTime getExpiredTime(GatewayPayParam payParam) {
        MchAppLocal mchAppLocal = PaymentContextLocal.get().getMchAppInfo();
        // 支付参数传入
        if (Objects.nonNull(payParam.getExpiredTime())) {
            return payParam.getExpiredTime();
        }
        // 根据商户应用配置计算出时间
        return PayUtil.getPaymentExpiredTime(mchAppLocal.getOrderTimeout());
    }

}
