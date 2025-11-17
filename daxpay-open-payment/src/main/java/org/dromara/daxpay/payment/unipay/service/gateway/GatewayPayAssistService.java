package org.dromara.daxpay.payment.unipay.service.gateway;

import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.common.util.TradeNoGenerateUtil;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderExpandManager;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrderExpand;
import org.dromara.daxpay.payment.pay.enums.PayRefundStatusEnum;
import org.dromara.daxpay.payment.pay.enums.PayStatusEnum;
import org.dromara.daxpay.payment.pay.result.gateway.GatewayOrderResult;
import org.dromara.daxpay.payment.pay.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.payment.pay.service.trade.pay.PayAssistService;
import org.dromara.daxpay.payment.unipay.param.gateway.GatewayPayParam;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    private final PayOrderExpandManager payOrderExpandManager;

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
        // 保存订单
        payOrderManager.save(order);
        // 保存订单扩展信息
        var orderExpand = new PayOrderExpand();
        orderExpand.setId(order.getId());
        payOrderExpandManager.save(orderExpand);
        // 注册支付超时任务
        delayJobService.registerByTransaction(order.getId(), DaxPayCode.Event.ORDER_PAY_TIMEOUT, order.getExpiredTime());
        return order;
    }


    /**
     * 查询订单状态
     */
    @IgnoreTenant
    public Boolean findStatusByOrderNo(String orderNo) {
        String status = payOrderManager.findByOrderNo(orderNo)
                .map(PayOrder::getStatus)
                .orElse(null);
        // 如果不是这三类状态则抛出异常
        if (!List.of(PayStatusEnum.WAIT.getCode(),PayStatusEnum.PROGRESS.getCode(),PayStatusEnum.SUCCESS.getCode()).contains(status)){
            throw new DataNotExistException("订单状态异常, 请重新支付! ");
        }
        return Objects.equals(status, PayStatusEnum.SUCCESS.getCode());
    }

    /**
     * 查询订单信息
     */
    @IgnoreTenant
    public GatewayOrderResult findOrderByOrderNo(String orderNo) {
        var payOrder = payOrderManager.findByOrderNo(orderNo).orElseThrow(() -> new DataNotExistException("订单不存在"));
        return new GatewayOrderResult()
                .setTitle(payOrder.getTitle())
                .setDescription(payOrder.getDescription())
                .setAmount(payOrder.getAmount())
                .setExpiredTime(payOrder.getExpiredTime())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOrderNo(payOrder.getOrderNo());
    }

}
